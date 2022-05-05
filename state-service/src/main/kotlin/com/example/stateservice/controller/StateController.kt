package com.example.stateservice.controller

import com.example.stateservice.entity.IdEntity
import com.example.stateservice.service.StateService
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/api/order")
class StateController(
    private val stateService: StateService
) {

    @PostMapping
    @ResponseBody
    fun create(): IdEntity {
        Thread.sleep(1000)
        return IdEntity(stateService.createOrder())
    }

    @PutMapping("{id}")
    @ResponseBody
    fun approve(@PathVariable id: Int) {
        Thread.sleep(1000)
        stateService.approveOrder(id)
    }

    @DeleteMapping("{id}")
    @ResponseBody
    fun reject(@PathVariable id: Int) {
        Thread.sleep(1000)
        stateService.rejectOrder(id)
    }

}
