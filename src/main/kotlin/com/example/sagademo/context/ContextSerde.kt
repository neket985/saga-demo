package com.example.sagademo.context

interface ContextSerde<T> {
    fun deserialize(data: ByteArray): T
    fun serialize(data: Any): ByteArray
}