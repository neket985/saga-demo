package com.example.stateservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StateServiceApplication

fun main(args: Array<String>) {
    runApplication<StateServiceApplication>(*args)
}
