package com.example.sagademo

import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaStepView
import com.fasterxml.jackson.databind.ObjectMapper

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate

@SpringBootTest
internal class SagaOrchestratorTest(
    private val sagaRepository: SagaRepository,
    private val sagaStepRepository: SagaStepRepository,
    private val sagaStepErrorRepository: SagaStepErrorRepository,
    private val tm: TransactionTemplate,
    private val mapper: ObjectMapper,
) {
    private val builder = SagaOrchestrator.builder(sagaRepository, sagaStepRepository, sagaStepErrorRepository, tm)

    @Test
    fun runNew() {
        val orchestrator = builder.setAlias("test")
            .addStep(JacksonContextSerde.jacksonContextSerde<User>(mapper), SagaStepView.compensatableView<User> {
                println()
            })
            .addStep(SagaStepView.compensatableView<User>(JacksonContextSerde.jacksonContextSerde(mapper)) {
                sampleService.checkUser(it!!)
            })
            .build()
    }
}