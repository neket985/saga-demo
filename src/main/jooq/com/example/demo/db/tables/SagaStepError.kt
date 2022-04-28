/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.tables


import com.example.demo.db.Public
import com.example.demo.db.keys.SAGA_STEP_ERROR_PKEY
import com.example.demo.db.keys.SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_ID_FKEY
import com.example.demo.db.keys.SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_STEP_ID_FKEY
import com.example.demo.db.tables.records.SagaStepErrorRecord

import java.time.LocalDateTime

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row6
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
open class SagaStepError(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, SagaStepErrorRecord>?,
    aliased: Table<SagaStepErrorRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<SagaStepErrorRecord>(
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
         * The reference instance of <code>public.saga_step_error</code>
         */
        val SAGA_STEP_ERROR = SagaStepError()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<SagaStepErrorRecord> = SagaStepErrorRecord::class.java

    /**
     * The column <code>public.saga_step_error.id</code>.
     */
    val ID: TableField<SagaStepErrorRecord, Int?> = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.saga_step_error.saga_id</code>.
     */
    val SAGA_ID: TableField<SagaStepErrorRecord, Int?> = createField(DSL.name("saga_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.saga_step_error.saga_step_id</code>.
     */
    val SAGA_STEP_ID: TableField<SagaStepErrorRecord, Int?> = createField(DSL.name("saga_step_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.saga_step_error.tries_counter</code>.
     */
    val TRIES_COUNTER: TableField<SagaStepErrorRecord, Int?> = createField(DSL.name("tries_counter"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.saga_step_error.description</code>.
     */
    val DESCRIPTION: TableField<SagaStepErrorRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "")

    /**
     * The column <code>public.saga_step_error.inserted_at</code>.
     */
    val INSERTED_AT: TableField<SagaStepErrorRecord, LocalDateTime?> = createField(DSL.name("inserted_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "")

    private constructor(alias: Name, aliased: Table<SagaStepErrorRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<SagaStepErrorRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.saga_step_error</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.saga_step_error</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.saga_step_error</code> table reference
     */
    constructor(): this(DSL.name("saga_step_error"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, SagaStepErrorRecord>): this(Internal.createPathAlias(child, key), child, key, SAGA_STEP_ERROR, null)
    override fun getSchema(): Schema = Public.PUBLIC
    override fun getIdentity(): Identity<SagaStepErrorRecord, Int?> = super.getIdentity() as Identity<SagaStepErrorRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<SagaStepErrorRecord> = SAGA_STEP_ERROR_PKEY
    override fun getKeys(): List<UniqueKey<SagaStepErrorRecord>> = listOf(SAGA_STEP_ERROR_PKEY)
    override fun getReferences(): List<ForeignKey<SagaStepErrorRecord, *>> = listOf(SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_ID_FKEY, SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_STEP_ID_FKEY)

    private lateinit var _saga: Saga
    private lateinit var _sagaStep: SagaStep
    fun saga(): Saga {
        if (!this::_saga.isInitialized)
            _saga = Saga(this, SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_ID_FKEY)

        return _saga;
    }
    fun sagaStep(): SagaStep {
        if (!this::_sagaStep.isInitialized)
            _sagaStep = SagaStep(this, SAGA_STEP_ERROR__SAGA_STEP_ERROR_SAGA_STEP_ID_FKEY)

        return _sagaStep;
    }
    override fun `as`(alias: String): SagaStepError = SagaStepError(DSL.name(alias), this)
    override fun `as`(alias: Name): SagaStepError = SagaStepError(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): SagaStepError = SagaStepError(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): SagaStepError = SagaStepError(name, null)

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?> = super.fieldsRow() as Row6<Int?, Int?, Int?, Int?, String?, LocalDateTime?>
}