package com.example.sagademo

import com.example.sagademo.context.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.step.SagaStepView.Companion.compensatableView
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.random.Random

@SpringBootTest(classes = [SagaTestConfiguration::class])
internal class SagaOrchestratorTest{

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var builder: SagaOrchestrator.Builder<Int, Int>

    @Test
    fun `simple correct test`() {
        var customNumber = 10

        val incrementStep = compensatableView<Int, Int>({
            ++customNumber
        }, { _, _ ->
            --customNumber
        })

        val orchestrator = builder.setAlias("test-correct-${UUID.randomUUID()}")
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .build()

        orchestrator.runNew(customNumber)
        assertEquals(12, customNumber)
    }

    @Test
    fun `rollback with 2 fails test`() {
        val expectedCustomNumber = Random.nextInt(100)
        var customNumber = expectedCustomNumber

        val incrementStep = compensatableView<Int, Int>({
            ++customNumber
        }, { i, _ ->
            --customNumber
            assertEquals(customNumber, i)
        })

        var strangeExceptionThrows = false
        val powStep = compensatableView<Int, Int>({
            customNumber = Math.pow(customNumber.toDouble(), 2.0).toInt()
            customNumber
        }, { i, _ ->
            if (!strangeExceptionThrows) {
                strangeExceptionThrows = true
                throw JokeException("ha-ha i'm so unexpected")
            }
            customNumber = Math.pow(customNumber.toDouble(), 0.5).toInt()
            assertEquals(customNumber, i)
        })

        val orchestrator = builder.setAlias("test-rollback-${UUID.randomUUID()}")
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), powStep)
            .addStep(jacksonContextSerde(mapper), incrementStep)
            .addStep(jacksonContextSerde(mapper), powStep)
            .addStep(jacksonContextSerde(mapper), compensatableView {
                val expectedNum = (expectedCustomNumber + 2).let { Math.pow(it.toDouble(), 2.0) + 1 }.let { Math.pow(it, 2.0) }.toInt()
                assertEquals(expectedNum, customNumber)
                throw JokeException("oops... i must rollback all operations... again...")
                1
            })
            .build()

        assertThrows<JokeException> {
            orchestrator.runNew(customNumber)
        }

        Thread.sleep(1000)

        orchestrator.selectBatchForRetry(10).forEach {
            assertThrows<JokeException> { orchestrator.run(it) }
        }

        Thread.sleep(1000)

        orchestrator.selectBatchForRetry(10).forEach {
            orchestrator.run(it)
        }


        assertEquals(expectedCustomNumber, customNumber)
    }

}