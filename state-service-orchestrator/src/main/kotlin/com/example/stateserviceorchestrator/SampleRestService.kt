package com.example.stateserviceorchestrator

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SampleRestService {
    private val restTemplate = RestTemplate()

    fun createOrder(): Int = restTemplate
        .exchange(
            RequestEntity.post("http://localhost:8080/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .body(IdEntity(1))
            ,
            IdEntity::class.java
        )
        .apply {
            if (statusCode != HttpStatus.OK) {
                error("wrong status code $statusCode")
            }
        }.body?.id ?: error("empty body")

    fun approveOrder(id: Int) {
        restTemplate
            .exchange("http://localhost:8080/api/order/$id", HttpMethod.PUT, HttpEntity.EMPTY, Unit::class.java)
            .apply {
                if (statusCode != HttpStatus.OK) {
                    error("wrong status code $statusCode")
                }
            }
    }

    fun rejectOrder(id: Int) {
        restTemplate
            .exchange("http://localhost:8080/api/order/$id", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)
            .apply {
                if (statusCode != HttpStatus.OK) {
                    error("wrong status code $statusCode")
                }
            }
    }
}