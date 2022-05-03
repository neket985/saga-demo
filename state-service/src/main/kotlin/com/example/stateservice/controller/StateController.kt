package com.example.stateservice.controller

import com.example.stateservice.service.StateService
import org.springframework.web.bind.annotation.*

@RestController("/api/order")
class StateController(
    private val stateService: StateService
) {

    @PostMapping
    fun create(): Int = stateService.createOrder()

    @PutMapping("{id}")
    fun approve(@PathVariable id: Int) = stateService.approveOrder(id)

    @DeleteMapping("{id}")
    fun reject(@PathVariable id: Int) = stateService.rejectOrder(id)

}
