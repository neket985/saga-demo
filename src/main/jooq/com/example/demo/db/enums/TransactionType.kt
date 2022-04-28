/*
 * This file is generated by jOOQ.
 */
package com.example.demo.db.enums


import com.example.demo.db.Public

import org.jooq.Catalog
import org.jooq.EnumType
import org.jooq.Schema


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
enum class TransactionType(@get:JvmName("literal") val literal: String) : EnumType {
    COMPENSATABLE("COMPENSATABLE"),
    RETRIABLE("RETRIABLE");
    override fun getCatalog(): Catalog? = schema.catalog
    override fun getSchema(): Schema = Public.PUBLIC
    override fun getName(): String = "transaction_type"
    override fun getLiteral(): String = literal
}
