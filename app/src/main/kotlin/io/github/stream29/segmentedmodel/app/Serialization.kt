package io.github.stream29.segmentedmodel.app

import io.github.stream29.jsonschemagenerator.SchemaGenerator
import io.github.stream29.jsonschemagenerator.schemaOf
import io.github.stream29.langchain4kt.core.Respondent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json { prettyPrint = true }
val schemaGenerator = SchemaGenerator()

inline fun <reified T> schemaOf() = json.encodeToString(schemaGenerator.schemaOf<T>())

inline fun <reified T> fromJson(text: String) = json.decodeFromString<T>(text)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
suspend inline fun <reified T> Respondent.chat(message: String) =
    fromJson<T>(chat(message))

suspend inline fun <reified T, reified R> Respondent.chat(input: T) =
    chat<R>(json.encodeToString(input))