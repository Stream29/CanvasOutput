package io.github.stream29.agent

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

class LoggingMutableList<T : Any>(
    val list: MutableList<T> = mutableListOf<T>()
) : MutableList<T> by list {
    @OptIn(ExperimentalSerializationApi::class)
    override fun add(element: T): Boolean {
        val serializer = serializer(element::class, emptyList<KSerializer<*>>(), false)
        println(json.encodeToString(serializer, element))
        return list.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { add(it) }
        return true
    }
}

fun <T : Any> loggingMutableListOf(vararg elements: T): LoggingMutableList<T> {
    return LoggingMutableList<T>().apply { addAll(elements) }
}