package com.example.sagademo.context

interface ContextSerde<I, O> {
    fun deserializeI(data: ByteArray): I
    fun deserializeO(data: ByteArray): O
    fun serialize(data: Any): ByteArray
}