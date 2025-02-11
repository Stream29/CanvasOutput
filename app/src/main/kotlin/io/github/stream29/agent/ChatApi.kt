package io.github.stream29.agent

import dev.langchain4j.model.dashscope.QwenChatModel
import io.github.stream29.langchain4kt.api.googlegemini.GeminiChatApiProvider
import io.github.stream29.langchain4kt.api.langchain4j.asChatApiProvider

val qwenApiKey = System.getenv("ALIBABA_QWEN_API_KEY") ?: throw RuntimeException("api key not found")

val qwenChatApiProvider =
    QwenChatModel.builder()
        .apiKey(qwenApiKey)
        .modelName("qwen-turbo-latest")
        .temperature(1.0f)
        .build()
        .asChatApiProvider()


val geminiApiKey = System.getenv("GOOGLE_AI_GEMINI_API_KEY") ?: throw RuntimeException("api key not found")

val geminiChatApiProvider =
    GeminiChatApiProvider(
        modelName = "gemini-2.0-flash",
        apiKey = geminiApiKey
    )