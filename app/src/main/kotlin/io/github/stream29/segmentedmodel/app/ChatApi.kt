package io.github.stream29.segmentedmodel.app

import dev.langchain4j.model.dashscope.QwenStreamingChatModel
import io.github.stream29.langchain4kt.api.langchain4j.asStreamChatApiProvider
import io.github.stream29.segmentedmodel.model.LoggingChatApiProvider

val apiKey = System.getenv("ALIBABA_QWEN_API_KEY") ?: throw RuntimeException("api key not found")
val qwenStreamChatApiProvider =
    QwenStreamingChatModel.builder()
        .apiKey(apiKey)
        .modelName("qwen-turbo")
        .temperature(1.0f)
        .build()
        .asStreamChatApiProvider()

val qwenChatApiProvider = LoggingChatApiProvider(qwenStreamChatApiProvider, loggingChannelProvider)