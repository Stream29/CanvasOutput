package io.github.stream29.segmentedmodel.model

import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.core.input.Context
import io.github.stream29.langchain4kt.core.output.Response
import io.github.stream29.langchain4kt.streaming.StreamChatApiProvider
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList

class LoggingChatApiProvider(
    val streamChatApiProvider: StreamChatApiProvider<*>,
    val loggingChannelProvider: () -> SendChannel<String>
) : ChatApiProvider<Nothing?> {
    override suspend fun generate(context: Context): Response<Nothing?> {
        val loggingChannel = loggingChannelProvider()
        return streamChatApiProvider.generate(context)
            .message
            .onEach { loggingChannel.send(it) }
            .onCompletion { loggingChannel.close() }
            .toList()
            .joinToString("")
            .let { Response(it, null) }
    }
}