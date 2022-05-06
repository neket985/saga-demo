package com.example.stateserviceorchestrator.controller

import com.example.stateserviceorchestrator.SampleOrchestrator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sampling")
class RunSamplingController(
    val sampleService: SampleOrchestrator
) {
    @GetMapping("run")
    fun run(
        @RequestParam(defaultValue = "1") count: Int,
        @RequestParam(defaultValue = "100") delay: Long
    ) {
        sampleService.runSamples(count, delay)
    }
}