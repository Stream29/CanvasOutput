package io.github.stream29.segmentedmodel.model

import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.core.input.Context
import io.github.stream29.langchain4kt.core.output.Response
import io.github.stream29.langchain4kt.streaming.StreamChatApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

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

inline fun loggingChannelMeta(
    loggingLock: Mutex,
    crossinline output: (String) -> Unit,
    crossinline onStart: () -> String = { "" },
    crossinline onComplete: () -> String = { "\n" },
): () -> Channel<String> = {
    Channel<String>().also {
        CoroutineScope(Dispatchers.IO).launch {
            it.consumeAsFlow()
                .buffer(Channel.UNLIMITED)
                .onStart {
                    loggingLock.lock()
                    output(onStart())
                }
                .onEach { output(it) }
                .onCompletion {
                    output(onComplete())
                    loggingLock.unlock()
                }
                .collect()
        }
    }
}