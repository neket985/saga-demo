package com.example.sagademo.context

import com.fasterxml.jackson.databind.ObjectMapper

class JacksonContextSerde<T>(
    val clazz: Class<T>,
    private val mapper: ObjectMapper
) : ContextSerde<T> {

    override fun deserialize(data: ByteArray): T = mapper.readValue(data, clazz)

    override fun serialize(data: Any): ByteArray = mapper.writeValueAsBytes(data)

    companion object {
        inline fun <reified T> jacksonContextSerde(mapper: ObjectMapper) = JacksonContextSerde(T::class.java, mapper)
    }
}