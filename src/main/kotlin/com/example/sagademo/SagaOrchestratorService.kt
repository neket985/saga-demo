package com.example.sagademo

import org.springframework.scheduling.annotation.Scheduled
import java.time.Duration


interface SagaOrchestratorService {
    val orchestrator: SagaOrchestrator<out Any>
    val batchSize: Int
    val operationTimeout: Duration

    @Scheduled(fixedDelay = 10)
    fun scheduleRetry(){
        orchestrator.selectBatchForRetry(batchSize).forEach(orchestrator::run)
    }

    @Scheduled(fixedDelay = 10)
    fun scheduleResetOldInProgress(){
        orchestrator.resetOldInProgress(operationTimeout)
    }
}