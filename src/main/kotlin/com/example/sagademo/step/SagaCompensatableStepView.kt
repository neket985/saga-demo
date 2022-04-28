package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType

interface SagaCompensatableStepView : SagaStepView {
    override val transactionType: TransactionType
        get() = TransactionType.COMPENSATABLE

    fun rollback(context: ByteArray?) {
        //do nothing
    }


    companion object {
        operator fun invoke(exec: (ByteArray?) -> ByteArray?) = object : SagaCompensatableStepView {
            override fun execute(context: ByteArray?) = exec(context)
        }

        operator fun invoke(exec: (ByteArray?) -> ByteArray?, rollbackL: (ByteArray?) -> Unit) = object : SagaCompensatableStepView {
            override fun execute(context: ByteArray?) = exec(context)
            override fun rollback(context: ByteArray?) = rollbackL(context)
        }
    }
}