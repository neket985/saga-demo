package com.example.sagademo

import com.example.sagademo.repository.SagaRepository
import com.example.sagademo.repository.SagaStepErrorRepository
import com.example.sagademo.repository.SagaStepRepository
import com.example.sagademo.strategy.ConstRetryStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration


@TestConfiguration
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(FlywayAutoConfiguration.FlywayConfiguration::class, SagaRepository::class, SagaStepRepository::class, SagaStepErrorRepository::class)
class SagaTestConfiguration {
    @Autowired
    private lateinit var sagaRepository: SagaRepository

    @Autowired
    private lateinit var sagaStepRepository: SagaStepRepository

    @Autowired
    private lateinit var sagaStepErrorRepository: SagaStepErrorRepository

    @Autowired
    private lateinit var tm: TransactionTemplate

    @Bean
    fun builder(): SagaOrchestrator.Builder<Int, Int> {
        return SagaOrchestrator.builder<Int>(sagaRepository, sagaStepRepository, sagaStepErrorRepository, tm)
            .setRetryStrategy(ConstRetryStrategy(Duration.ofSeconds(1)))
    }
}