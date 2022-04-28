package com.example.sagademo.sample

import com.example.sagademo.SagaOrchestrator
import com.example.sagademo.context.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaStepView.Companion.compensatableView
import com.example.sagademo.step.SagaStepView.Companion.retriableView
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import javax.annotation.PostConstruct

/**
 * Простой пример сервиса, использующего сагу для регистрации пользователя
 * 1) заводим сущность пользователя в бд (предварительная регистрация)
 * 2) проводим проверку возможности его регистрации
 * 3) обновляем статус в бд
 * 4) направляем уведомление пользователю
 */
@Service
class SampleOrchestrator(
    private val sampleService: SampleService,
    sagaRepo: SagaRepository,
    sagaStepRepo: SagaStepRepository,
    sagaStepErrorRepository: SagaStepErrorRepository,
    tm: TransactionTemplate,
    mapper: ObjectMapper,
) {
    private val orchestrator = SagaOrchestrator.Builder<User>(sagaRepo, sagaStepRepo, sagaStepErrorRepository, tm)
        .setAlias("sample1")
        .addStep(jacksonContextSerde(mapper), compensatableView{ sampleService.createUser(it!!) })
        .addStep(jacksonContextSerde(mapper), compensatableView { sampleService.checkUser(it!!) })
        .addStep(jacksonContextSerde(mapper), retriableView{ sampleService.approveUser(it!!) })
        .addStep(jacksonContextSerde(mapper), retriableView<String, Any> { sampleService.notifyUser(it!!) })
        .build()

    /*
    todo schedulers:
                        1) retry
                        2) reset old in process
     */
}