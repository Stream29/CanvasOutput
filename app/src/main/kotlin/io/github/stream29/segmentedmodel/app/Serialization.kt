package io.github.stream29.segmentedmodel.app

import io.github.stream29.jsonschemagenerator.toSchema
import io.github.stream29.langchain4kt.core.Respondent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

val json = Json { prettyPrint = true }

inline fun <reified T> schemaOf() = json.encodeToString(serializer<T>().descriptor.toSchema())

inline fun <reified T> fromJson(text: String) = json.decodeFromString<T>(text)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
suspend inline fun <reified T> Respondent.chat(message: String) =
    fromJson<T>(chat(message))