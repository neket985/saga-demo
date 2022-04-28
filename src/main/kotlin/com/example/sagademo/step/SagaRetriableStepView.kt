package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType

interface SagaRetriableStepView : SagaStepView {
    override val transactionType: TransactionType
        get() = TransactionType.RETRIABLE

    companion object {
        operator fun invoke(exec: (ByteArray?) -> ByteArray?) = object : SagaRetriableStepView {
            override fun execute(context: ByteArray?) = exec(context)
        }
    }
}