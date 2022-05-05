package com.example.stateservice.repository

import com.example.demo.db.enums.OrderFiscalState
import com.example.demo.db.tables.daos.OrderFiscalDao
import com.example.demo.db.tables.pojos.OrderFiscal
import com.example.demo.db.tables.references.ORDER_FISCAL
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class OrderFiscalRepository(
    private val dsl: DSLContext
) : OrderFiscalDao(dsl.configuration()) {
    fun insertReturningId(order: OrderFiscal) =
        dsl.insertQuery(ORDER_FISCAL)
            .apply {
                setRecord(dsl.newRecord(ORDER_FISCAL, order))
                setReturning(ORDER_FISCAL.ID)
                execute()
            }.returnedRecord!!.id!!

    fun changeStateById(id: Int, state: OrderFiscalState) =
        dsl.update(ORDER_FISCAL)
            .set(ORDER_FISCAL.STATE, state)
            .where(ORDER_FISCAL.ID.eq(id))
            .execute()
}