/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db


import kotlin.collections.List

import org.jooq.Schema
import org.jooq.impl.CatalogImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class DefaultCatalog : CatalogImpl("") {
    companion object {

        /**
         * The reference instance of <code>DEFAULT_CATALOG</code>
         */
        val DEFAULT_CATALOG = DefaultCatalog()
    }

    /**
     * The schema <code>order_fiscal_service</code>.
     */
    val ORDER_FISCAL_SERVICE get() = OrderFiscalService.ORDER_FISCAL_SERVICE

    override fun getSchemas(): List<Schema> = listOf(
        OrderFiscalService.ORDER_FISCAL_SERVICE
    )
}
