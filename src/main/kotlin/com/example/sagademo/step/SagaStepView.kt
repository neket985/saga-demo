package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType
import com.example.sagademo.context.ContextSerde

interface SagaStepView<I, O> {
    val transactionType: TransactionType

    fun execute(serde: ContextSerde<I, O>, context: ByteArray?): ByteArray? = execute(context?.let { serde.deserializeI(it) })?.let {
        serde.serialize(it)
    }

    fun execute(context: I?): O?

    companion object {
        inline fun <reified I, O> compensatableView(crossinline exec: (I?) -> O?) =
            SagaCompensatableStepView<I, O> { exec(it) }

        inline fun <reified I, O> compensatableView(crossinline exec: (I?) -> O?, crossinline rollback: (I?, O?) -> Unit) =
            SagaCompensatableStepView<I, O>({ exec(it) }, { i, o -> rollback(i, o) })

        inline fun <reified I, O> retriableView(crossinline exec: (I?) -> O?) =
            SagaRetriableStepView<I, O> { exec(it) }
    }
}