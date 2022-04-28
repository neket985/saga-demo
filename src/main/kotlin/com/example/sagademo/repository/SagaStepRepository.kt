package com.example.sagademo.repository

import com.example.demo.db.enums.StepCompletionType
import com.example.demo.db.tables.daos.SagaStepDao
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.references.SAGA_STEP
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.util.cubrid.CUBRIDDSL.incr
import org.springframework.stereotype.Repository

@Repository
class SagaStepRepository(
    private val dsl: DSLContext
) : SagaStepDao(dsl.configuration()) {
    fun getBySagaIdAndStepNumber(sagaId: Int, stepNumber: Int) =
        dsl.select(*SAGA_STEP.fields())
            .where(
                SAGA_STEP.SAGA_ID.eq(sagaId)
                    .and(SAGA_STEP.STEP_NUMBER.eq(stepNumber))
            ).fetchOneInto(SagaStep::class.java)

    fun updateStateById(id: Int, state: StepCompletionType) =
        dsl.update(SAGA_STEP)
            .set(SAGA_STEP.COMPLETION_STATE, state)
            .where(SAGA_STEP.ID.eq(id))
            .execute()

    fun updateContextById(id: Int, context: ByteArray?) =
        dsl.update(SAGA_STEP)
            .set(SAGA_STEP.CONTEXT, context)
            .where(SAGA_STEP.ID.eq(id))
            .execute()

    fun updateStateByIdAndIncrementTriesCount(id: Int, state: StepCompletionType) =
        dsl.update(SAGA_STEP)
            .set(SAGA_STEP.COMPLETION_STATE, state)
            .set(SAGA_STEP.TRIES_COUNT, SAGA_STEP.TRIES_COUNT.plus(1))
            .where(SAGA_STEP.ID.eq(id))
            .execute()
}