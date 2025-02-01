package io.github.stream29.agent

import dev.langchain4j.model.dashscope.QwenChatModel
import io.github.stream29.langchain4kt.api.langchain4j.asChatApiProvider

val apiKey = System.getenv("ALIBABA_QWEN_API_KEY") ?: throw RuntimeException("api key not found")

val qwenChatApiProvider =
    QwenChatModel.builder()
        .apiKey(apiKey)
        .modelName("qwen-max")
        .temperature(1.0f)
        .build()
        .asChatApiProvider()