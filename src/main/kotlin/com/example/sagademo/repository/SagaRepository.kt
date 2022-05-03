package com.example.sagademo.repository

import com.example.demo.db.enums.CompletionType
import com.example.demo.db.tables.Saga.Companion.SAGA
import com.example.demo.db.tables.daos.SagaDao
import com.example.demo.db.tables.pojos.Saga
import com.example.demo.db.tables.pojos.SagaStep
import com.example.demo.db.tables.records.SagaRecord
import com.example.demo.db.tables.references.SAGA_STEP
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class SagaRepository(
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
            .set(SAGA.TRIES_COUNT, SAGA.TRIES_COUNT.plus(1))
            .set(SAGA.NEXT_TRIES_AT, nextTriesAt)
            .where(SAGA.ID.eq(id))
            .execute()

    fun insertReturningId(saga: Saga) = insertReturningId(dsl.newRecord(SAGA, saga))
    fun insertReturningId(saga: SagaRecord) =
        dsl.insertQuery(SAGA).apply {
            addRecord(saga)
            setReturning(SAGA.ID)
            execute()
        }.returnedRecord!!.id!!

    @Transactional
    fun selectForRetryWithUpdatingState(alias: String, limit: Int): List<SagaWithSteps> =
        dsl.update(SAGA)
            .set(SAGA.COMPLETION_STATE, CompletionType.IN_PROGRESS)
            .set(SAGA.UPDATED_AT, LocalDateTime.now())
            .where(
                SAGA.ID.`in`(
                    dsl.select(SAGA.ID)
                        .from(SAGA)
                        .where(SAGA.ORCHESTRATOR_ALIAS.eq(alias))
                        .and(SAGA.COMPLETION_STATE.eq(CompletionType.ERROR))
                        .and(SAGA.NEXT_TRIES_AT.lessThan(LocalDateTime.now()))
                        .limit(limit)
                        .forUpdate()//заблокируем выбранные записи, чтобы больше их никто не выбрал
                        .skipLocked()//пропустим все записи, которые заблокировались другими соединениями
                )
            )
            .returning(SAGA.ID)
            .fetch()
            .map { it.id!! }
            .let { idsToRetry ->
                fetchByIdJoinSteps(*idsToRetry.toTypedArray())
            }

    fun fetchByIdJoinSteps(vararg id: Int?) =
        dsl.select(*SAGA.fields(), *SAGA_STEP.fields())
            .from(SAGA)
            .leftJoin(SAGA_STEP)
            .on(SAGA_STEP.SAGA_ID.eq(SAGA.ID))
            .where(SAGA.ID.`in`(*id))
            .fetchGroups(SAGA.ID)
            .map { (_, entry) ->
                val first = entry.first()
                val saga = first.into(SAGA).into(Saga::class.java)
                val steps =
                    if (first[SAGA_STEP.ID] != null) entry.map { it.into(SAGA_STEP).into(SagaStep::class.java) }
                    else emptyList()
                SagaWithSteps(saga, steps)
            }

    fun resetOldInProgressAfterTime(alias: String, resetAfter: LocalDateTime) =
        dsl.update(SAGA)
            .set(SAGA.COMPLETION_STATE, CompletionType.ERROR)
            .set(SAGA.TRIES_COUNT, SAGA.TRIES_COUNT.plus(1))
            .where(SAGA.ORCHESTRATOR_ALIAS.eq(alias))
            .and(SAGA.UPDATED_AT.greaterThan(resetAfter))
            .and(SAGA.COMPLETION_STATE.eq(CompletionType.IN_PROGRESS))
            .returning(SAGA.ID, SAGA.TRIES_COUNT)
            .fetch()
            .map { it.id to it.triesCount }
}