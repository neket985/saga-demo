package com.example.stateserviceorchestrator

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SampleFiscalRestService {
    private val restTemplate = RestTemplate()

    fun createFiscal(orderId: Int): Int = restTemplate
        .exchange("http://localhost:8002/api/fiscal", HttpMethod.POST, HttpEntity(IdEntity(orderId)), IdEntity::class.java)
        .apply {
            if (statusCode != HttpStatus.OK) {
                error("wrong status code $statusCode")
            }
        }.body?.id ?: error("empty body")

    fun approveFiscal(id: Int) {
        restTemplate
            .exchange("http://localhost:8002/api/fiscal/$id", HttpMethod.PUT, HttpEntity.EMPTY, Unit::class.java)
            .apply {
                if (statusCode != HttpStatus.OK) {
                    error("wrong status code $statusCode")
                }
            }
    }

    fun rejectFiscal(id: Int) {
        restTemplate
            .exchange("http://localhost:8002/api/fiscal/$id", HttpMethod.DELETE, HttpEntity.EMPTY, Unit::class.java)
            .apply {
                if (statusCode != HttpStatus.OK) {
                    error("wrong status code $statusCode")
                }
            }
    }
}