package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.SchemaGenerator
import io.github.stream29.jsonschemagenerator.schemaOf
import io.github.stream29.langchain4kt.core.Respondent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}
val schemaGenerator = SchemaGenerator()

inline fun <reified T> schemaOf() = json.encodeToString(schemaGenerator.schemaOf<T>())

inline fun <reified T> fromJson(text: String) = json.decodeFromString<T>(text)

fun String.normalizedJsonOutput() =
    when {
        startsWith("```") -> substringAfter("\n").substringBeforeLast("```")
        startsWith("{\n{") -> substringAfter("\n")
        else -> this
    }