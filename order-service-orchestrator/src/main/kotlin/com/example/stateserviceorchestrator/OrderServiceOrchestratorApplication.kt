package com.example.stateserviceorchestrator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example"])
class OrderServiceOrchestratorApplication

fun main(args: Array<String>) {
    runApplication<OrderServiceOrchestratorApplication>(*args)
}
