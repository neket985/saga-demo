package com.example.sagademo

import com.fasterxml.jackson.databind.ObjectMapper

class JacksonContextSerde<T>(
    val clazz: Class<T>,
    private val mapper: ObjectMapper
) : ContextSerde<T> {

    override fun deserialize(data: ByteArray): T = mapper.readValue(data, clazz)

    override fun serialize(data: T): ByteArray = mapper.writeValueAsBytes(data)

}