package com.example.stateserviceorchestrator

import com.example.sagademo.SagaOrchestrator
import com.example.sagademo.SagaOrchestratorService
import com.example.sagademo.context.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaStepView.Companion.compensatableView
import com.example.sagademo.step.SagaStepView.Companion.retriableView
import com.example.sagademo.strategy.ExponentialRetryStrategy
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

/**
 * Простой пример сервиса, использующего сагу для регистрации пользователя
 * 1) заводим сущность пользователя в бд (предварительная регистрация)
 * 2) проводим проверку возможности его регистрации
 * 3) обновляем статус в бд
 * 4) направляем уведомление пользователю
 */
@Service
@EnableScheduling
class SampleOrchestrator(
    sagaRepository: SagaRepository,
    sagaStepRepository: SagaStepRepository,
    sagaStepErrorRepository: SagaStepErrorRepository,
    tm: TransactionTemplate,
    mapper: ObjectMapper,
    private val rest: SampleRestService
) : SagaOrchestratorService {
    override val orchestrator = SagaOrchestrator.builder<Unit>(
        sagaRepository,
        sagaStepRepository,
        sagaStepErrorRepository,
        tm
    )
        .setAlias("sample")
        .setRetryStrategy(ExponentialRetryStrategy(Duration.ofSeconds(1), 2.0, 10.0))
        .addStep(jacksonContextSerde(mapper), compensatableView({
            rest.createOrder()
        }, { _, o ->
            if (o != null) {
                rest.rejectOrder(o)
            }
        }))
        .addStep(jacksonContextSerde(mapper), retriableView { orderId ->
            orderId!!
            rest.approveOrder(orderId)
        })
        .build()

    override val retryBatchSize: Int = 100
    override val inProgressTimeout: Duration = Duration.ofMinutes(5)

    @PostConstruct
    fun test() {
        val executor = ThreadPoolExecutor(10, 10, 1, TimeUnit.DAYS, LinkedBlockingQueue(100), ThreadPoolExecutor.CallerRunsPolicy())
        Thread {
            while (true) {
                executor.execute {
                    orchestrator.runNew()
                }
                Thread.sleep(500)
            }
        }.start()
    }
}