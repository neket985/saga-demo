package com.example.sagademo.step

import com.example.demo.db.enums.TransactionType

interface SagaStepView {
    val transactionType: TransactionType

    fun execute(context: ByteArray?): ByteArray?
}