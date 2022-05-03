/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db


import com.example.demo.db.sequences.SHIP_ORDER_ID_SEQ
import com.example.demo.db.tables.ShipOrder

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class StateService : SchemaImpl("state_service", DefaultCatalog.DEFAULT_CATALOG) {
    companion object {

        /**
         * The reference instance of <code>state_service</code>
         */
        val STATE_SERVICE = StateService()
    }

    /**
     * The table <code>state_service.ship_order</code>.
     */
    val SHIP_ORDER get() = ShipOrder.SHIP_ORDER

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getSequences(): List<Sequence<*>> = listOf(
        SHIP_ORDER_ID_SEQ
    )

    override fun getTables(): List<Table<*>> = listOf(
        ShipOrder.SHIP_ORDER
    )
}
