package com.example.sagademo.context

import com.fasterxml.jackson.databind.ObjectMapper

class JacksonContextSerde<I, O>(
    val clazzI: Class<I>,
    val clazzO: Class<O>,
    private val mapper: ObjectMapper
) : ContextSerde<I, O> {

    override fun deserializeI(data: ByteArray): I = mapper.readValue(data, clazzI)
    override fun deserializeO(data: ByteArray): O = mapper.readValue(data, clazzO)

    override fun serialize(data: Any): ByteArray = mapper.writeValueAsBytes(data)

    companion object {
        inline fun <reified I, reified O> jacksonContextSerde(mapper: ObjectMapper) = JacksonContextSerde(I::class.java, O::class.java, mapper)
    }
}