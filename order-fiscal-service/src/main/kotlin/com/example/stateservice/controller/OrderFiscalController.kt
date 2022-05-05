package com.example.stateservice.controller

import com.example.stateservice.entity.IdEntity
import com.example.stateservice.service.OrderFiscalService
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/api/fiscal")
class OrderFiscalController(
    private val orderFiscalService: OrderFiscalService
) {

    @PostMapping
    @ResponseBody
    fun create(@RequestBody body: IdEntity): IdEntity {
        val result =  IdEntity(orderFiscalService.create(body.id))
        Thread.sleep(1000)
        return result
    }

    @PutMapping("{id}")
    @ResponseBody
    fun approve(@PathVariable id: Int) {
        orderFiscalService.approve(id)
        Thread.sleep(1000)
    }

    @DeleteMapping("{id}")
    @ResponseBody
    fun reject(@PathVariable id: Int) {
        orderFiscalService.reject(id)
        Thread.sleep(1000)
    }

}
