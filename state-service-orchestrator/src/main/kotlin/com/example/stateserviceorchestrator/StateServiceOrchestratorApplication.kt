package com.example.stateserviceorchestrator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example"])
class StateServiceOrchestratorApplication

fun main(args: Array<String>) {
    runApplication<StateServiceOrchestratorApplication>(*args)
}
