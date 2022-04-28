package com.example.sagademo

import com.example.demo.db.enums.CompletionType
import com.example.demo.db.enums.StepCompletionType
import com.example.demo.db.enums.TransactionType
import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.pojos.SagaStepError
import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaCompensatableStepView
import com.example.sagademo.step.SagaStepView
import org.slf4j.LoggerFactory
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

open class SagaOrchestrator(
    private val orchestratorAlias: String,
    private val sagaRepo: SagaRepository,
    private val sagaStepRepo: SagaStepRepository,
    private val sagaStepErrorRepository: SagaStepErrorRepository,
    private val tm: TransactionTemplate,
    private vararg val stepsView: SagaStepView
) {
    private val logger = LoggerFactory.getLogger(SagaOrchestrator::class.java)

    fun runNew(initContext: ByteArray?) {
        val sagaId = insertSagaWithSteps(initContext)
        val saga = sagaRepo.getByIdJoinSteps(sagaId)!!
        run(saga)
    }

    fun run(saga: SagaWithSteps) {
        runCatching {
            checkAndExecute(saga)
            sagaRepo.updateStateById(saga.saga.id!!, CompletionType.COMPLETE)
        }.onFailure {
            sagaRepo.updateStateAndSetNextTriesById(saga.saga.id!!, CompletionType.ERROR, LocalDateTime.now().plusSeconds(30)) //todo сделать постепенный рост задержки между ретраями
        }.getOrThrow()
    }

    // проверяем сагу, чтобы понять, на чем мы остановились и куда дальше идти (продолжение сценария/откат)
    fun checkAndExecute(saga: SagaWithSteps) {
        logger.debug("Check saga #${saga.saga.id}")
        stepsView.indices.forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")

            when (step.completionState) {
                StepCompletionType.WAIT -> return completeSaga(saga, stepNumber)
                StepCompletionType.ROLLBACK -> return rollbackSaga(saga, stepNumber)
                StepCompletionType.ERROR -> {
                    return when (stepsView[stepNumber].transactionType) {
                        TransactionType.COMPENSATABLE -> rollbackSaga(saga, stepNumber)
                        TransactionType.RETRIABLE -> completeSaga(saga, stepNumber)
                    }
                }
                else -> {
                    //skip
                }
            }
        }

        logger.warn("Saga #${saga.saga.id} always succeed")
    }


    //продолжить выполнение сценария
    fun completeSaga(saga: SagaWithSteps, fromStep: Int) {
        logger.debug("Complete saga #${saga.saga.id} from step $fromStep")
        (fromStep until stepsView.size).forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")
            val nextStep = saga.getStep(stepNumber + 1)
            tryCompleteStep(saga.saga.id!!, step, nextStep)
        }
    }

    //выполнить откат всех шагов
    fun rollbackSaga(saga: SagaWithSteps, fromStep: Int) {
        logger.debug("Rollback saga #${saga.saga.id} from step $fromStep")
        (0..fromStep).reversed().forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")
            tryRollbackStep(saga.saga.id!!, step)
            sagaStepRepo.updateStateById(step.id!!, StepCompletionType.ROLLBACK)
        }
    }

    fun tryCompleteStep(sagaId: Int, step: SagaStep, nextStep: SagaStep?) = tryExecStep(sagaId, step) {
        val view = stepsView[step.stepNumber!!]
        val nextCtx = view.execute(step.context)
        tm.execute {
            sagaStepRepo.updateStateById(step.id!!, StepCompletionType.SUCCESS)
            nextStep?.apply {
                //обновляем контекст следующего шага как в бд, так и в памяти
                sagaStepRepo.updateContextById(this.id!!, nextCtx)
                nextStep.context = nextCtx
            }
        }
        nextCtx
    }

    fun tryRollbackStep(sagaId: Int, step: SagaStep) = tryExecStep(sagaId, step) {
        val view = stepsView[step.stepNumber!!]
        if (view !is SagaCompensatableStepView) error("wrong step view type ${view::class.java}. it must be ${SagaCompensatableStepView::class.java}")
        view.rollback(step.context)
        sagaStepRepo.updateStateById(step.id!!, StepCompletionType.ROLLBACK)
    }

    fun <T> tryExecStep(sagaId: Int, step: SagaStep, exec: () -> T): T =
        runCatching {
            exec()
        }.onFailure { err ->
            tm.execute {
                sagaStepRepo.updateStateByIdAndIncrementTriesCount(step.id!!, StepCompletionType.ERROR)
                sagaStepErrorRepository.insert(
                    SagaStepError(
                        sagaId = sagaId,
                        sagaStepId = step.id,
                        triesCounter = step.triesCount,
                        description = err.stackTraceToString()
                    )
                )
            }
        }.getOrThrow()


    private fun insertSagaWithSteps(initContext: ByteArray?): Int {
        val saga = Saga(
            orchestratorAlias = orchestratorAlias,
            completionState = CompletionType.IN_PROGRESS
        )

        return tm.execute {
            val sagaId = sagaRepo.insertReturningId(saga)

            val steps = stepsView.mapIndexed { stepNumber, step ->
                SagaStep(
                    sagaId = sagaId,
                    stepNumber = stepNumber,
                    transactionType = step.transactionType,
                    completionState = StepCompletionType.WAIT,
                    context = if (stepNumber == 0) initContext else null
                )
            }
            sagaStepRepo.insert(*steps.toTypedArray())
            return@execute sagaId
        }!!
    }
}