package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType
import com.example.sagademo.context.ContextSerde

interface SagaCompensatableStepView<I, O> : SagaStepView<I, O> {
    override val transactionType: TransactionType
        get() = TransactionType.COMPENSATABLE

    fun rollback(serde: ContextSerde<out I, out O>, iContext: ByteArray?, oContext: ByteArray?) =
        rollback(iContext?.let { serde.deserializeI(it) }, oContext?.let { serde.deserializeO(it) })

    fun rollback(iContext: I?, oContext: O?) {
        //do nothing
    }


    companion object {
        operator fun <I, O> invoke(exec: (I?) -> O?) = object : SagaCompensatableStepView<I, O> {
            override fun execute(context: I?) = exec(context)
        }

        operator fun <I, O> invoke(exec: (I?) -> O?, rollbackL: (I?, O?) -> Unit) = object : SagaCompensatableStepView<I, O> {
            override fun execute(context: I?) = exec(context)
            override fun rollback(iContext: I?, oContext: O?) = rollbackL(iContext, oContext)
        }
    }
}