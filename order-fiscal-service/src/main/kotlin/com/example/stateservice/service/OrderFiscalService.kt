package com.example.stateservice.service

import com.example.demo.db.enums.OrderFiscalState
import com.example.demo.db.tables.pojos.OrderFiscal
import com.example.stateservice.repository.OrderFiscalRepository
import org.springframework.stereotype.Service

@Service
class OrderFiscalService(
    private val fiscalRepository: OrderFiscalRepository
) {

    fun create(orderId: Int): Int =
        fiscalRepository.insertReturningId(
            OrderFiscal(
                orderId = orderId,
                state = OrderFiscalState.FISCALIZED_PENDING
            )
        )

    fun approve(id: Int) {
        fiscalRepository.changeStateById(id, OrderFiscalState.FISCALIZED)
    }

    fun reject(id: Int) {
        fiscalRepository.changeStateById(id, OrderFiscalState.REJECTED)
    }
}