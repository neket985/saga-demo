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
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration

@Service
class SampleOrchestrator(
    sagaRepository: SagaRepository,
    sagaStepRepository: SagaStepRepository,
    sagaStepErrorRepository: SagaStepErrorRepository,
    tm: TransactionTemplate,
    mapper: ObjectMapper,
    private val rest: SampleRestService
) : SagaOrchestratorService {
    override val orchestrator = SagaOrchestrator.Builder<Unit>(
        sagaRepository,
        sagaStepRepository,
        sagaStepErrorRepository,
        tm
    )
        .setAlias("sample")
        .setRetryStrategy(ExponentialRetryStrategy(Duration.ofSeconds(1), 2.0, 10.0))
        .addStep(jacksonContextSerde(mapper), compensatableView ({
            rest.createOrder()
        }, {
//            rest.rejectOrder() todo id?
        }))
        .addStep(jacksonContextSerde(mapper), retriableView { orderId ->
            orderId!!
            rest.approveOrder(orderId)
        })
        .build()

    override val batchSize: Int = 100
    override val operationTimeout: Duration = Duration.ofMinutes(5)
}