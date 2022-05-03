package com.example.stateservice.service

import com.example.demo.db.enums.OrderState
import com.example.demo.db.tables.pojos.ShipOrder
import com.example.stateservice.repository.ShipOrderRepository
import org.springframework.stereotype.Service

@Service
class StateService(
    private val orderRepository: ShipOrderRepository
) {

    fun createOrder(): Int =
        orderRepository.insertReturningId(
            ShipOrder(
                state = OrderState.CREATED_PENDING
            )
        )

    fun approveOrder(id: Int) {
        orderRepository.changeStateById(id, OrderState.CREATED)
    }

    fun rejectOrder(id: Int) {
        orderRepository.changeStateById(id, OrderState.REJECTED)
    }
}