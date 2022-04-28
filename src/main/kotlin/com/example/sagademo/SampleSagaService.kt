package com.example.sagademo

import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaCompensatableStepView
import com.example.sagademo.step.SagaRetriableStepView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
class SampleSagaService(
    private val sampleService: SampleService,
    sagaRepo: SagaRepository,
    sagaStepRepo: SagaStepRepository,
    sagaStepErrorRepository: SagaStepErrorRepository,
    tm: TransactionTemplate,
) : SagaOrchestrator(
    "simple", sagaRepo, sagaStepRepo, sagaStepErrorRepository, tm,
    compensatableView<User, User> { sampleService.createUser(it!!) },
    compensatableView<User, CheckedUser> { sampleService.checkUser(it!!) },
    retriableView<CheckedUser, String> { sampleService.approveUser(it!!) },
    retriableView<String, Unit> { sampleService.notifyUser(it!!) }
) {
    @PostConstruct
    fun test() {
        runNew(mapper.writeValueAsBytes(User(1, "test")))
        println("done")
    }

    companion object {
        val mapper = jacksonObjectMapper()
        inline fun <reified I, O> compensatableView(crossinline exec: (I?) -> O) = SagaCompensatableStepView { input ->
            val mapped = input?.let { mapper.readValue<I>(it) }
            val result = exec(mapped)
            mapper.writeValueAsBytes(result)
        }

        inline fun <reified I, O> retriableView(crossinline exec: (I?) -> O) = SagaRetriableStepView { input ->
            val mapped = input?.let { mapper.readValue<I>(it) }
            val result = exec(mapped)
            mapper.writeValueAsBytes(result)
        }
    }
}