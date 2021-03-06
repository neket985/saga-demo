/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.tables


import com.example.demo.db.Public
import com.example.demo.db.enums.StepCompletionType
import com.example.demo.db.enums.TransactionType
import com.example.demo.db.indexes.SAGA_ID_AND_STEP_NUMBER_UINDEX
import com.example.demo.db.keys.SAGA_STEP_PKEY
import com.example.demo.db.keys.SAGA_STEP__SAGA_STEP_SAGA_ID_FKEY
import com.example.demo.db.tables.records.SagaStepRecord

import java.time.LocalDateTime

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Index
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row9
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class SagaStep(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, SagaStepRecord>?,
    aliased: Table<SagaStepRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<SagaStepRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.saga_step</code>
         */
        val SAGA_STEP = SagaStep()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<SagaStepRecord> = SagaStepRecord::class.java

    /**
     * The column <code>public.saga_step.id</code>.
     */
    val ID: TableField<SagaStepRecord, Int?> = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.saga_step.saga_id</code>.
     */
    val SAGA_ID: TableField<SagaStepRecord, Int?> = createField(DSL.name("saga_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.saga_step.step_number</code>.
     */
    val STEP_NUMBER: TableField<SagaStepRecord, Int?> = createField(DSL.name("step_number"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.saga_step.completion_state</code>.
     */
    val COMPLETION_STATE: TableField<SagaStepRecord, StepCompletionType?> = createField(DSL.name("completion_state"), SQLDataType.VARCHAR.asEnumDataType(com.example.demo.db.enums.StepCompletionType::class.java), this, "")

    /**
     * The column <code>public.saga_step.tries_count</code>.
     */
    val TRIES_COUNT: TableField<SagaStepRecord, Int?> = createField(DSL.name("tries_count"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "")

    /**
     * The column <code>public.saga_step.transaction_type</code>.
     */
    val TRANSACTION_TYPE: TableField<SagaStepRecord, TransactionType?> = createField(DSL.name("transaction_type"), SQLDataType.VARCHAR.asEnumDataType(com.example.demo.db.enums.TransactionType::class.java), this, "")

    /**
     * The column <code>public.saga_step.context</code>.
     */
    val CONTEXT: TableField<SagaStepRecord, ByteArray?> = createField(DSL.name("context"), SQLDataType.BLOB, this, "")

    /**
     * The column <code>public.saga_step.inserted_at</code>.
     */
    val INSERTED_AT: TableField<SagaStepRecord, LocalDateTime?> = createField(DSL.name("inserted_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "")

    /**
     * The column <code>public.saga_step.updated_at</code>.
     */
    val UPDATED_AT: TableField<SagaStepRecord, LocalDateTime?> = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "")

    private constructor(alias: Name, aliased: Table<SagaStepRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<SagaStepRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.saga_step</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.saga_step</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.saga_step</code> table reference
     */
    constructor(): this(DSL.name("saga_step"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, SagaStepRecord>): this(Internal.createPathAlias(child, key), child, key, SAGA_STEP, null)
    override fun getSchema(): Schema = Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(SAGA_ID_AND_STEP_NUMBER_UINDEX)
    override fun getIdentity(): Identity<SagaStepRecord, Int?> = super.getIdentity() as Identity<SagaStepRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<SagaStepRecord> = SAGA_STEP_PKEY
    override fun getKeys(): List<UniqueKey<SagaStepRecord>> = listOf(SAGA_STEP_PKEY)
    override fun getReferences(): List<ForeignKey<SagaStepRecord, *>> = listOf(SAGA_STEP__SAGA_STEP_SAGA_ID_FKEY)

    private lateinit var _saga: Saga
    fun saga(): Saga {
        if (!this::_saga.isInitialized)
            _saga = Saga(this, SAGA_STEP__SAGA_STEP_SAGA_ID_FKEY)

        return _saga;
    }
    override fun `as`(alias: String): SagaStep = SagaStep(DSL.name(alias), this)
    override fun `as`(alias: Name): SagaStep = SagaStep(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): SagaStep = SagaStep(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): SagaStep = SagaStep(name, null)

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row9<Int?, Int?, Int?, StepCompletionType?, Int?, TransactionType?, ByteArray?, LocalDateTime?, LocalDateTime?> = super.fieldsRow() as Row9<Int?, Int?, Int?, StepCompletionType?, Int?, TransactionType?, ByteArray?, LocalDateTime?, LocalDateTime?>
}
