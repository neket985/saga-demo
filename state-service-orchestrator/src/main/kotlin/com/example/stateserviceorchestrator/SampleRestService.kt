package com.example.stateserviceorchestrator

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SampleRestService(
    private val restTemplate: RestTemplate
) {
    fun createOrder(): Int = restTemplate
        .exchange("http://localhost:8080/api/order", HttpMethod.POST, null, IdEntity::class.java)
        .apply {
            if(statusCode!=HttpStatus.OK){
                error("wrong status code $statusCode")
            }
        }.body?.id ?: error("empty body")

    fun approveOrder(id: Int) = restTemplate
        .exchange("http://localhost:8080/api/order/$id", HttpMethod.PUT, null, Unit::class.java)
        .apply {
            if(statusCode!=HttpStatus.OK){
                error("wrong status code $statusCode")
            }
        }

    fun rejectOrder(id: Int) = restTemplate
        .exchange("http://localhost:8080/api/order/$id", HttpMethod.DELETE, null, Unit::class.java)
        .apply {
            if(statusCode!=HttpStatus.OK){
                error("wrong status code $statusCode")
            }
        }
}