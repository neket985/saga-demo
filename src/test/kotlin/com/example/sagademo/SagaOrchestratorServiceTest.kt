package com.example.sagademo

import com.example.sagademo.context.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.step.SagaStepView.Companion.retriableView
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.util.*

@SpringBootTest(classes = [SagaTestConfiguration::class])
internal class SagaOrchestratorServiceTest {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var builder: SagaOrchestrator.Builder<Int, Int>

    @Test
    fun `resetting old in progress`() {
        val orchestrator = builder
            .setAlias("test-reset-${UUID.randomUUID()}")
            .addStep(jacksonContextSerde(mapper), retriableView {
                Thread.sleep(10_000_000) //sleep for toooo long
            })
            .build()

        Thread {
            orchestrator.runNew()
        }.start()
        Thread.sleep(1_000)

        val duration = Duration.ofSeconds(5)
        val resetted1 = orchestrator.resetOldInProgress(duration)!!
        assertEquals(0, resetted1.size)
        Thread.sleep(duration.toMillis())
        val resetted2 = orchestrator.resetOldInProgress(duration)!!
        assertEquals(1, resetted2.size)
        val resetted3 = orchestrator.resetOldInProgress(duration)!!
        assertEquals(0, resetted3.size)

        val forRetry = orchestrator.selectBatchForRetry(10)
        assertEquals(1, forRetry.size)
    }
}