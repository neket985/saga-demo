/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.tables.records


import com.example.demo.db.enums.CompletionType
import com.example.demo.db.tables.Saga

import java.time.LocalDateTime

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record7
import org.jooq.Row7
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class SagaRecord() : UpdatableRecordImpl<SagaRecord>(Saga.SAGA), Record7<Int?, String?, CompletionType?, Int?, LocalDateTime?, LocalDateTime?, LocalDateTime?> {

    var id: Int?
        set(value) = set(0, value)
        get() = get(0) as Int?

    var orchestratorAlias: String?
        set(value) = set(1, value)
        get() = get(1) as String?

    var completionState: CompletionType?
        set(value) = set(2, value)
        get() = get(2) as CompletionType?

    var triesCount: Int?
        set(value) = set(3, value)
        get() = get(3) as Int?

    var nextTriesAt: LocalDateTime?
        set(value) = set(4, value)
        get() = get(4) as LocalDateTime?

    var insertedAt: LocalDateTime?
        set(value) = set(5, value)
        get() = get(5) as LocalDateTime?

    var updatedAt: LocalDateTime?
        set(value) = set(6, value)
        get() = get(6) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row7<Int?, String?, CompletionType?, Int?, LocalDateTime?, LocalDateTime?, LocalDateTime?> = super.fieldsRow() as Row7<Int?, String?, CompletionType?, Int?, LocalDateTime?, LocalDateTime?, LocalDateTime?>
    override fun valuesRow(): Row7<Int?, String?, CompletionType?, Int?, LocalDateTime?, LocalDateTime?, LocalDateTime?> = super.valuesRow() as Row7<Int?, String?, CompletionType?, Int?, LocalDateTime?, LocalDateTime?, LocalDateTime?>
    override fun field1(): Field<Int?> = Saga.SAGA.ID
    override fun field2(): Field<String?> = Saga.SAGA.ORCHESTRATOR_ALIAS
    override fun field3(): Field<CompletionType?> = Saga.SAGA.COMPLETION_STATE
    override fun field4(): Field<Int?> = Saga.SAGA.TRIES_COUNT
    override fun field5(): Field<LocalDateTime?> = Saga.SAGA.NEXT_TRIES_AT
    override fun field6(): Field<LocalDateTime?> = Saga.SAGA.INSERTED_AT
    override fun field7(): Field<LocalDateTime?> = Saga.SAGA.UPDATED_AT
    override fun component1(): Int? = id
    override fun component2(): String? = orchestratorAlias
    override fun component3(): CompletionType? = completionState
    override fun component4(): Int? = triesCount
    override fun component5(): LocalDateTime? = nextTriesAt
    override fun component6(): LocalDateTime? = insertedAt
    override fun component7(): LocalDateTime? = updatedAt
    override fun value1(): Int? = id
    override fun value2(): String? = orchestratorAlias
    override fun value3(): CompletionType? = completionState
    override fun value4(): Int? = triesCount
    override fun value5(): LocalDateTime? = nextTriesAt
    override fun value6(): LocalDateTime? = insertedAt
    override fun value7(): LocalDateTime? = updatedAt

    override fun value1(value: Int?): SagaRecord {
        this.id = value
        return this
    }

    override fun value2(value: String?): SagaRecord {
        this.orchestratorAlias = value
        return this
    }

    override fun value3(value: CompletionType?): SagaRecord {
        this.completionState = value
        return this
    }

    override fun value4(value: Int?): SagaRecord {
        this.triesCount = value
        return this
    }

    override fun value5(value: LocalDateTime?): SagaRecord {
        this.nextTriesAt = value
        return this
    }

    override fun value6(value: LocalDateTime?): SagaRecord {
        this.insertedAt = value
        return this
    }

    override fun value7(value: LocalDateTime?): SagaRecord {
        this.updatedAt = value
        return this
    }

    override fun values(value1: Int?, value2: String?, value3: CompletionType?, value4: Int?, value5: LocalDateTime?, value6: LocalDateTime?, value7: LocalDateTime?): SagaRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        return this
    }

    /**
     * Create a detached, initialised SagaRecord
     */
    constructor(id: Int? = null, orchestratorAlias: String? = null, completionState: CompletionType? = null, triesCount: Int? = null, nextTriesAt: LocalDateTime? = null, insertedAt: LocalDateTime? = null, updatedAt: LocalDateTime? = null): this() {
        this.id = id
        this.orchestratorAlias = orchestratorAlias
        this.completionState = completionState
        this.triesCount = triesCount
        this.nextTriesAt = nextTriesAt
        this.insertedAt = insertedAt
        this.updatedAt = updatedAt
    }
}
