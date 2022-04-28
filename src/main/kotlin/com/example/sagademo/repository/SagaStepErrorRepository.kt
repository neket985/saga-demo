package com.example.sagademo.repository

import com.example.demo.db.tables.daos.SagaDao
import com.example.demo.db.tables.daos.SagaStepErrorDao
import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.records.SagaRecord
import com.example.demo.db.tables.references.SAGA
import com.example.demo.db.tables.references.SAGA_STEP
import com.example.sagademo.SagaWithSteps
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SagaStepErrorRepository(
    private val dsl: DSLContext
) : SagaStepErrorDao(dsl.configuration()) {

}