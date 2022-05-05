package com.example.sagademo

import com.example.sagademo.context.JacksonContextSerde.Companion.jacksonContextSerde
import com.example.sagademo.step.SagaStepView.Companion.retriableView
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

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

    @Test
    fun `test conflicts retry async`() {
        val executor = ThreadPoolExecutor(20, 20, 0, TimeUnit.DAYS, LinkedBlockingQueue(100), ThreadPoolExecutor.CallerRunsPolicy())

        val orchestrator = builder
            .setAlias("test-retry-async-${UUID.randomUUID()}")
            .addStep(jacksonContextSerde(mapper), retriableView {
                throw JokeException("so, retry it (¬‿¬)")
                Unit
            })
            .build()


        val total = 1000
        (0 until total).map{
            executor.submit {
                runCatching { orchestrator.runNew() }
            }
        }.forEach { it.get() }
        LoggerFactory.getLogger(this::class.java).info("tasks prepared for retry")

        Thread.sleep(1_000)

        val batchSize = 20
        val batchesCount = total / batchSize

        val futures = (0 until (batchesCount * 1.5).toInt()).map {
            val f = CompletableFuture<List<Int>>()
            executor.submit {
                val selected = orchestrator.selectBatchForRetry(batchSize)
                f.complete(selected.map { it.saga.id!! })
            }
            f
        }.map { it.join() }

        assertEquals(total, futures.sumOf { it.size })
        assertEquals(total, futures.flatten().distinct().size)
    }
}