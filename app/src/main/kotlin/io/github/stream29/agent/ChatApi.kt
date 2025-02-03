package io.github.stream29.agent

import dev.langchain4j.model.dashscope.QwenChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.github.stream29.langchain4kt.api.langchain4j.asChatApiProvider

val qwenApiKey = System.getenv("ALIBABA_QWEN_API_KEY") ?: throw RuntimeException("api key not found")

val qwenChatApiProvider =
    QwenChatModel.builder()
        .apiKey(qwenApiKey)
        .modelName("qwen-max-latest")
        .temperature(1.0f)
        .build()
        .asChatApiProvider()

val deepSeekApiKey = System.getenv("DEEPSEEK_API_KEY") ?: throw RuntimeException("api key not found")

val deepSeekChatApiProvider =
    OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com/v1")
        .apiKey(deepSeekApiKey)
        .modelName("deepseek-chat")
        .build()
        .asChatApiProvider()