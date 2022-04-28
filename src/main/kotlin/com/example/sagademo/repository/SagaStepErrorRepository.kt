package com.example.sagademo.repository

import com.example.demo.db.tables.daos.SagaStepErrorDao
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class SagaStepErrorRepository(
    private val dsl: DSLContext
) : SagaStepErrorDao(dsl.configuration()) {

}