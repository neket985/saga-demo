package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType
import com.example.sagademo.ContextSerde

interface SagaCompensatableStepView<I, O> : SagaStepView<I, O> {
    override val transactionType: TransactionType
        get() = TransactionType.COMPENSATABLE

    fun rollback(serde: ContextSerde<out I>, context: ByteArray?) = rollback(context?.let { serde.deserialize(it) })
    fun rollback(context: I?) {
        //do nothing
    }


    companion object {
        operator fun <I, O> invoke(exec: (I?) -> O?) = object : SagaCompensatableStepView<I, O> {
            override fun execute(context: I?) = exec(context)
        }

        operator fun <I, O> invoke(exec: (I?) -> O?, rollbackL: (I?) -> Unit) = object : SagaCompensatableStepView<I, O> {
            override fun execute(context: I?) = exec(context)
            override fun rollback(context: I?) = rollbackL(context)
        }
    }
}