package com.example.sagademo.repository

import com.example.demo.db.enums.CompletionType
import com.example.demo.db.enums.StepCompletionType
import com.example.demo.db.tables.daos.SagaDao
import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.records.SagaRecord
import com.example.demo.db.tables.references.SAGA
import com.example.demo.db.tables.references.SAGA_STEP
import com.example.sagademo.SagaWithSteps
import org.jooq.DSLContext
import org.jooq.util.cubrid.CUBRIDDSL.incr
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class SagaRepository(
    private val stepRepo: SagaStepRepository,
    private val dsl: DSLContext
) : SagaDao(dsl.configuration()) {

    fun updateStateById(id: Int, state: CompletionType) =
        dsl.update(SAGA)
            .set(SAGA.COMPLETION_STATE, state)
            .where(SAGA.ID.eq(id))
            .execute()

    fun updateStateAndSetNextTriesById(id: Int, state: CompletionType, nextTriesAt: LocalDateTime) =
        dsl.update(SAGA)
            .set(SAGA.COMPLETION_STATE, state)
            .set(SAGA.TRIES_COUNT, incr(SAGA.TRIES_COUNT))
            .set(SAGA.NEXT_TRIES_AT, nextTriesAt)
            .where(SAGA.ID.eq(id))
            .execute()

    fun insertReturningId(saga: Saga) = insertReturningId(dsl.newRecord(SAGA, saga))
    fun insertReturningId(saga: SagaRecord) =
        dsl.insertQuery(SAGA).apply {
            addRecord(saga)
//            setDefaultValues()
            setReturning(SAGA.ID)
            execute()
        }.returnedRecord!!.id!!

    fun getByIdJoinSteps(id: Int) =
        dsl.select(*SAGA.fields(), *SAGA_STEP.fields())
            .from(SAGA)
            .leftJoin(SAGA_STEP)
            .on(SAGA_STEP.SAGA_ID.eq(SAGA.ID))
            .where(SAGA.ID.eq(id))
            .fetchGroups(SAGA.ID)
            .map { (_, entry) ->
                val first = entry.first()
                val saga = first.into(Saga::class.java)
                val steps =
                    if (first[SAGA_STEP.ID] != null) entry.map { it.into(SagaStep::class.java) }
                    else emptyList()
                SagaWithSteps(saga, steps)
            }.firstOrNull()

}