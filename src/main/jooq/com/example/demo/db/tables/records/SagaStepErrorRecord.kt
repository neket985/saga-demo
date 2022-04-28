/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.tables.records


import com.example.demo.db.tables.SagaStepError

import java.time.LocalDateTime

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record6
import org.jooq.Row6
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class SagaStepErrorRecord() : UpdatableRecordImpl<SagaStepErrorRecord>(SagaStepError.SAGA_STEP_ERROR), Record6<Int?, Int?, Int?, Int?, String?, LocalDateTime?> {

    var id: Int?
        set(value) = set(0, value)
        get() = get(0) as Int?

    var sagaId: Int?
        set(value) = set(1, value)
        get() = get(1) as Int?

    var sagaStepId: Int?
        set(value) = set(2, value)
        get() = get(2) as Int?

    var triesCounter: Int?
        set(value) = set(3, value)
        get() = get(3) as Int?

    var description: String?
        set(value) = set(4, value)
        get() = get(4) as String?

    var insertedAt: LocalDateTime?
        set(value) = set(5, value)
        get() = get(5) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?> = super.fieldsRow() as Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?>
    override fun valuesRow(): Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?> = super.valuesRow() as Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?>
    override fun field1(): Field<Int?> = SagaStepError.SAGA_STEP_ERROR.ID
    override fun field2(): Field<Int?> = SagaStepError.SAGA_STEP_ERROR.SAGA_ID
    override fun field3(): Field<Int?> = SagaStepError.SAGA_STEP_ERROR.SAGA_STEP_ID
    override fun field4(): Field<Int?> = SagaStepError.SAGA_STEP_ERROR.TRIES_COUNTER
    override fun field5(): Field<String?> = SagaStepError.SAGA_STEP_ERROR.DESCRIPTION
    override fun field6(): Field<LocalDateTime?> = SagaStepError.SAGA_STEP_ERROR.INSERTED_AT
    override fun component1(): Int? = id
    override fun component2(): Int? = sagaId
    override fun component3(): Int? = sagaStepId
    override fun component4(): Int? = triesCounter
    override fun component5(): String? = description
    override fun component6(): LocalDateTime? = insertedAt
    override fun value1(): Int? = id
    override fun value2(): Int? = sagaId
    override fun value3(): Int? = sagaStepId
    override fun value4(): Int? = triesCounter
    override fun value5(): String? = description
    override fun value6(): LocalDateTime? = insertedAt

    override fun value1(value: Int?): SagaStepErrorRecord {
        this.id = value
        return this
    }

    override fun value2(value: Int?): SagaStepErrorRecord {
        this.sagaId = value
        return this
    }

    override fun value3(value: Int?): SagaStepErrorRecord {
        this.sagaStepId = value
        return this
    }

    override fun value4(value: Int?): SagaStepErrorRecord {
        this.triesCounter = value
        return this
    }

    override fun value5(value: String?): SagaStepErrorRecord {
        this.description = value
        return this
    }

    override fun value6(value: LocalDateTime?): SagaStepErrorRecord {
        this.insertedAt = value
        return this
    }

    override fun values(value1: Int?, value2: Int?, value3: Int?, value4: Int?, value5: String?, value6: LocalDateTime?): SagaStepErrorRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        return this
    }

    /**
     * Create a detached, initialised SagaStepErrorRecord
     */
    constructor(id: Int? = null, sagaId: Int? = null, sagaStepId: Int? = null, triesCounter: Int? = null, description: String? = null, insertedAt: LocalDateTime? = null): this() {
        this.id = id
        this.sagaId = sagaId
        this.sagaStepId = sagaStepId
        this.triesCounter = triesCounter
        this.description = description
        this.insertedAt = insertedAt
    }
}