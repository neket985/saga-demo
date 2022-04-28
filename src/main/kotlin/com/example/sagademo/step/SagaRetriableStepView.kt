package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType

interface SagaRetriableStepView<I, O> : SagaStepView<I, O> {
    override val transactionType: TransactionType
        get() = TransactionType.RETRIABLE

    companion object {
        operator fun <I, O> invoke(exec: (I?) -> O?) = object : SagaRetriableStepView<I, O> {
            override fun execute(context: I?) = exec(context)
        }
    }
}