package com.example.sagademo

import com.example.demo.db.enums.CompletionType
import com.example.demo.db.enums.StepCompletionType
import com.example.demo.db.enums.TransactionType
import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.pojos.SagaStepError
import com.example.sagademo.context.ContextSerde
import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.repository.SagaWithSteps
import com.example.sagademo.step.SagaCompensatableStepView
import com.example.sagademo.step.SagaStepView
import com.example.sagademo.strategy.ExponentialRetryStrategy
import com.example.sagademo.strategy.RetryStrategy
import org.slf4j.LoggerFactory
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

open class SagaOrchestrator private constructor(
    val orchestratorAlias: String,
    private val sagaRepo: SagaRepository,
    private val sagaStepRepo: SagaStepRepository,
    private val sagaStepErrorRepository: SagaStepErrorRepository,
    private val tm: TransactionTemplate,
    private val retryStrategy: RetryStrategy,
    private val stepsView: List<Pair<SagaStepView<*, *>, ContextSerde<*, *>>>
) {
    private val logger = LoggerFactory.getLogger(SagaOrchestrator::class.java)

    fun runNew(initContext: Any?, serde: ContextSerde<*, *>) = runNew(initContext?.let { serde.serialize(it) })
    private fun runNew(initContext: ByteArray?) {
        val sagaId = insertSagaWithSteps(initContext)
        val saga = sagaRepo.fetchByIdJoinSteps(sagaId).first()
        run(saga)
    }

    fun selectBatchForRetry(batchSize: Int): List<SagaWithSteps> =
        sagaRepo.selectForRetryWithUpdatingState(orchestratorAlias, batchSize)

    fun resetOldInProgress(operationTimeout: Duration) {
        tm.execute {
            val updated = sagaRepo.resetOldInProgressAfterTime(orchestratorAlias, LocalDateTime.now().minusSeconds(operationTimeout.seconds))
            val errors = updated
                .map { (sagaId, triesCount) ->
                    SagaStepError(
                        sagaId = sagaId,
                        triesCounter = triesCount,
                        description = "Saga timed out after ${operationTimeout.seconds} seconds"
                    )
                }

            sagaStepErrorRepository.insert(errors)
        }
    }

    fun run(saga: SagaWithSteps) {
        runCatching {
            checkAndExecute(saga)
            sagaRepo.updateStateById(saga.saga.id!!, CompletionType.COMPLETE)
        }.onFailure {
            val nextTryAt = LocalDateTime.now().plusSeconds(retryStrategy.getNextTime(saga.saga.triesCount!!).seconds)
            sagaRepo.updateStateAndSetNextTriesById(saga.saga.id!!, CompletionType.ERROR, nextTryAt)
        }.getOrThrow()
    }

    // проверяем сагу, чтобы понять, на чем мы остановились и куда дальше идти (продолжение сценария/откат)
    private fun checkAndExecute(saga: SagaWithSteps) {
        logger.debug("Check saga #${saga.saga.id}")
        stepsView.indices.forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")

            when (step.completionState) {
                StepCompletionType.WAIT -> return completeSaga(saga, stepNumber)
                StepCompletionType.ROLLBACK -> return rollbackSaga(saga, stepNumber)
                StepCompletionType.ERROR -> {
                    when (stepsView[stepNumber].first.transactionType) {
                        TransactionType.COMPENSATABLE -> return rollbackSaga(saga, stepNumber)
                        TransactionType.RETRIABLE -> return completeSaga(saga, stepNumber)
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
    private fun completeSaga(saga: SagaWithSteps, fromStep: Int) {
        logger.debug("Complete saga #${saga.saga.id} from step $fromStep")
        (fromStep until stepsView.size).forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")
            val nextStep = saga.getStep(stepNumber + 1)
            tryCompleteStep(saga.saga.id!!, step, nextStep)
        }
    }

    //выполнить откат всех шагов
    private fun rollbackSaga(saga: SagaWithSteps, fromStep: Int) {
        logger.debug("Rollback saga #${saga.saga.id} from step $fromStep")
        (0..fromStep).reversed().forEach { stepNumber ->
            val step = saga.getStep(stepNumber) ?: error("not found step $stepNumber for saga #${saga.saga.id}")
            tryRollbackStep(saga.saga.id!!, step)
            sagaStepRepo.updateStateById(step.id!!, StepCompletionType.ROLLBACK)
        }
    }

    private fun tryCompleteStep(sagaId: Int, step: SagaStep, nextStep: SagaStep?) = tryExecStep(sagaId, step) {
        val (view, serde) = stepsView[step.stepNumber!!]
        @Suppress("UNCHECKED_CAST")//fixme types mismatch
        val nextCtx = (view as SagaStepView<Any, Any>).execute(serde as ContextSerde<Any, Any>, step.inputContext)
        tm.execute {
            sagaStepRepo.updateStateAndOutputContextById(step.id!!, StepCompletionType.SUCCESS, nextCtx)
            nextStep?.apply {
                //обновляем контекст следующего шага как в бд, так и в памяти
                sagaStepRepo.updateInputContextById(this.id!!, nextCtx)
                nextStep.inputContext = nextCtx
            }
        }
        nextCtx
    }

    private fun tryRollbackStep(sagaId: Int, step: SagaStep) = tryExecStep(sagaId, step) {
        val (view, serde) = stepsView[step.stepNumber!!]
        if (view !is SagaCompensatableStepView) error("wrong step view type ${view::class.java}. it must be ${SagaCompensatableStepView::class.java}")
        @Suppress("UNCHECKED_CAST")//fixme types mismatch
        (view as SagaCompensatableStepView<Any, Any>).rollback(serde as ContextSerde<Any, Any>, step.inputContext, step.outputContext)
        sagaStepRepo.updateStateById(step.id!!, StepCompletionType.ROLLBACK)
    }

    private fun <T> tryExecStep(sagaId: Int, step: SagaStep, exec: () -> T): T =
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

            val steps = stepsView.mapIndexed { stepNumber, (view, _) ->
                SagaStep(
                    sagaId = sagaId,
                    stepNumber = stepNumber,
                    transactionType = view.transactionType,
                    completionState = StepCompletionType.WAIT,
                    inputContext = if (stepNumber == 0) initContext else null
                )
            }
            sagaStepRepo.insert(*steps.toTypedArray())
            return@execute sagaId
        }!!
    }

    class Builder<I>(
        private val sagaRepo: SagaRepository,
        private val sagaStepRepo: SagaStepRepository,
        private val sagaStepErrorRepository: SagaStepErrorRepository,
        private val tm: TransactionTemplate,

        private var retryStrategy: RetryStrategy = ExponentialRetryStrategy(Duration.ofMinutes(1), 2.0, 10.0),
        private var alias: String = UUID.randomUUID().toString(),
        private val views: List<Pair<SagaStepView<*, *>, ContextSerde<*, *>>> = listOf()
    ) {

        fun setAlias(alias: String): Builder<I> {
            this.alias = alias
            return this
        }

        fun setRetryStrategy(retryStrategy: RetryStrategy): Builder<I> {
            this.retryStrategy = retryStrategy
            return this
        }

        fun <O> addStep(serde: ContextSerde<out I, O>, view: SagaStepView<out I, O>): Builder<O> {
            return Builder(sagaRepo, sagaStepRepo, sagaStepErrorRepository, tm, retryStrategy, alias, views.plus(view to serde))
        }

        fun validate() {
            if (views.isEmpty()) error("Saga must contains at least one steps")
            var prevView: SagaStepView<*, *>? = null
            views.forEach { (view, _) ->
                if (prevView?.transactionType == TransactionType.RETRIABLE
                    && view.transactionType == TransactionType.COMPENSATABLE
                ) {
                    error("Steps flow is wrong. COMPENSATABLE steps only allows before RETRIABLE.")
                }
                prevView = view
            }
        }

        fun build(): SagaOrchestrator {
            validate()

            return SagaOrchestrator(alias, sagaRepo, sagaStepRepo, sagaStepErrorRepository, tm, retryStrategy, views)
        }
    }
}