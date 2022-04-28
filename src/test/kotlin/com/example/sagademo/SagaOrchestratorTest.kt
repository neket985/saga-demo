package com.example.sagademo

import com.example.sagademo.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.step.SagaStepView.Companion.compensatableView
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import kotlin.random.Random

@SpringBootTest
internal class SagaOrchestratorTest() {
    @Autowired
    private lateinit var sagaRepository: SagaRepository

    @Autowired
    private lateinit var sagaStepRepository: SagaStepRepository

    @Autowired
    private lateinit var sagaStepErrorRepository: SagaStepErrorRepository

    @Autowired
    private lateinit var tm: TransactionTemplate

    @Autowired
    private lateinit var mapper: ObjectMapper

    private val builder by lazy {
        SagaOrchestrator.Builder<Any>(sagaRepository, sagaStepRepository, sagaStepErrorRepository, tm)
    }

    @Test
    fun `simple correct test`() {
        var customNumber = 10

        val incrementStep = compensatableView<Any, Any>({
            customNumber++
        }, {
            customNumber--
        })

        val orchestrator = builder.setAlias("test")
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .build()

        orchestrator.runNew(customNumber, jacksonContextSerde<Any>(mapper))
        assertEquals(12, customNumber)
    }

    @Test
    fun `rollback test`() {
        val expectedCustomNumber = Random.nextInt(100)
        var customNumber = expectedCustomNumber

        val incrementStep = compensatableView<Int, Int>({
            ++customNumber
        }, {
            --customNumber
            assertEquals(customNumber, it)
        })

        val powStep = compensatableView<Int, Int>({
            customNumber = Math.pow(customNumber.toDouble(), 2.0).toInt()
            customNumber
        }, {
            customNumber = Math.pow(customNumber.toDouble(), 0.5).toInt()
            assertEquals(customNumber, it)
        })

        val orchestrator = builder.setAlias("test")
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), powStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), powStep)
            .addStep(jacksonContextSerde(mapper), compensatableView {
                val expectedNum = (expectedCustomNumber + 2).let { Math.pow(it.toDouble(), 2.0) + 1 }.let { Math.pow(it, 2.0) }.toInt()
                assertEquals(expectedNum, customNumber)
                throw JokeException("oops... i must rollback all operations... again...")
            })
            .build()

        assertThrows<JokeException> {
            orchestrator.runNew(customNumber, jacksonContextSerde<Any>(mapper))
        }

        Thread.sleep(1000)

        orchestrator.selectBatchForRetry(10).forEach {
            orchestrator.run(it)
        }

        assertEquals(expectedCustomNumber, customNumber)
    }
}