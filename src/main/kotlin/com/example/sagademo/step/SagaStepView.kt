package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType
import com.example.sagademo.ContextSerde

interface SagaStepView<I, O> {
    val transactionType: TransactionType

    fun execute(serde: ContextSerde<I>, context: ByteArray?): ByteArray? = execute(context?.let { serde.deserialize(it) })?.let {
        serde.serialize(it)
    }

    fun execute(context: I?): O?

    companion object {
        inline fun <reified I, O> compensatableView(crossinline exec: (I?) -> O?) =
            SagaCompensatableStepView<I, O> { exec(it) }

        inline fun <reified I, O> compensatableView(crossinline exec: (I?) -> O?, crossinline rollback: (I?) -> Unit) =
            SagaCompensatableStepView<I, O>({ exec(it) }, { rollback(it) })

        inline fun <reified I, O> retriableView(crossinline exec: (I?) -> O?) =
            SagaRetriableStepView<I, O> { exec(it) }
    }
}