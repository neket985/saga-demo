package com.example.sagademo

interface ContextSerde<T> {
    fun deserialize(data: ByteArray): T
    fun serialize(data: T): ByteArray
}