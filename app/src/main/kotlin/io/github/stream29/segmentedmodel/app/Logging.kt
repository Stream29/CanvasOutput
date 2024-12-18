package io.github.stream29.segmentedmodel.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

val loggingLock = Mutex()
val loggingChannelProvider = {
    val channel = Channel<String>()
    CoroutineScope(Dispatchers.IO).launch {
        channel.consumeAsFlow()
            .buffer(Channel.UNLIMITED)
            .onStart { loggingLock.lock() }
            .onEach { log(it) }
            .onCompletion { log("\n") }
            .onCompletion { loggingLock.unlock() }
            .collect()
    }
    channel
}

fun log(message: String) {
    print(message)
}