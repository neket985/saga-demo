/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.tables


import com.example.demo.db.StateService
import com.example.demo.db.enums.OrderState
import com.example.demo.db.keys.SHIP_ORDER_PKEY
import com.example.demo.db.tables.records.ShipOrderRecord

import java.time.LocalDateTime

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row4
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
open class ShipOrder(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, ShipOrderRecord>?,
    aliased: Table<ShipOrderRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<ShipOrderRecord>(
    alias,
    StateService.STATE_SERVICE,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>state_service.ship_order</code>
         */
        val SHIP_ORDER = ShipOrder()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<ShipOrderRecord> = ShipOrderRecord::class.java

    /**
     * The column <code>state_service.ship_order.id</code>.
     */
    val ID: TableField<ShipOrderRecord, Int?> = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>state_service.ship_order.state</code>.
     */
    val STATE: TableField<ShipOrderRecord, OrderState?> = createField(DSL.name("state"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(com.example.demo.db.enums.OrderState::class.java), this, "")

    /**
     * The column <code>state_service.ship_order.inserted_at</code>.
     */
    val INSERTED_AT: TableField<ShipOrderRecord, LocalDateTime?> = createField(DSL.name("inserted_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "")

    /**
     * The column <code>state_service.ship_order.updated_at</code>.
     */
    val UPDATED_AT: TableField<ShipOrderRecord, LocalDateTime?> = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "")

    private constructor(alias: Name, aliased: Table<ShipOrderRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<ShipOrderRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>state_service.ship_order</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>state_service.ship_order</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>state_service.ship_order</code> table reference
     */
    constructor(): this(DSL.name("ship_order"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, ShipOrderRecord>): this(Internal.createPathAlias(child, key), child, key, SHIP_ORDER, null)
    override fun getSchema(): Schema = StateService.STATE_SERVICE
    override fun getIdentity(): Identity<ShipOrderRecord, Int?> = super.getIdentity() as Identity<ShipOrderRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<ShipOrderRecord> = SHIP_ORDER_PKEY
    override fun getKeys(): List<UniqueKey<ShipOrderRecord>> = listOf(SHIP_ORDER_PKEY)
    override fun `as`(alias: String): ShipOrder = ShipOrder(DSL.name(alias), this)
    override fun `as`(alias: Name): ShipOrder = ShipOrder(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): ShipOrder = ShipOrder(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): ShipOrder = ShipOrder(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<Int?, OrderState?, LocalDateTime?, LocalDateTime?> = super.fieldsRow() as Row4<Int?, OrderState?, LocalDateTime?, LocalDateTime?>
}
