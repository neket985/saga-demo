package com.example.stateservice.repository

import com.example.demo.db.enums.OrderState
import com.example.demo.db.tables.daos.ShipOrderDao
import com.example.demo.db.tables.pojos.ShipOrder
import com.example.demo.db.tables.references.SHIP_ORDER
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class ShipOrderRepository(
    private val dsl: DSLContext
) : ShipOrderDao(dsl.configuration()) {
    fun insertReturningId(order: ShipOrder) =
        dsl.insertQuery(SHIP_ORDER)
            .apply {
                setRecord(dsl.newRecord(SHIP_ORDER, order))
                setReturning(SHIP_ORDER.ID)
                execute()
            }.returnedRecord!!.id!!

    fun changeStateById(id: Int, state: OrderState) =
        dsl.update(SHIP_ORDER)
            .set(SHIP_ORDER.STATE, state)
            .where(SHIP_ORDER.ID.eq(id))
}