package io.github.stream29.agent

import dev.langchain4j.model.dashscope.QwenChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.github.stream29.langchain4kt.api.googlegemini.GeminiChatApiProvider
import io.github.stream29.langchain4kt.api.langchain4j.asChatApiProvider
import java.time.Duration

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
        .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
        .apiKey(qwenApiKey)
        .modelName("deepseek-v3")
        .timeout(Duration.ofSeconds(600))
        .build()
        .asChatApiProvider()

val geminiApiKey = System.getenv("GOOGLE_AI_GEMINI_API_KEY") ?: throw RuntimeException("api key not found")

val geminiChatApiProvider =
    GeminiChatApiProvider(
        modelName = "gemini-2.0-flash",
        apiKey = geminiApiKey
    )