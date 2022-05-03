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
     * The schema <code>saga_scheme</code>.
     */
    val SAGA_SCHEME get() = SagaScheme.SAGA_SCHEME

    override fun getSchemas(): List<Schema> = listOf(
        SagaScheme.SAGA_SCHEME
    )
}
