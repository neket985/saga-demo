package com.example.sagademo

import com.example.sagademo.repository.SagaRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SagaDemoApplicationTests {
    @Autowired
    private lateinit var sagaRepository: SagaRepository

    @Test
    fun contextLoads() {
        val q = sagaRepository.fetchByIdJoinSteps(1)
        println(q)
    }

}
