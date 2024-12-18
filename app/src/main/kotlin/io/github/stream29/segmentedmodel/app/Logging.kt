package io.github.stream29.segmentedmodel.app

import io.github.stream29.segmentedmodel.model.loggingChannelMeta
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock

fun titledSplitter(title: Any?) = "${"-".repeat(10)}$title${"-".repeat(10)}"

val loggingChannelProvider = run {
    val startTime = Clock.System.now()
    loggingChannelMeta(
        loggingLock = Mutex(),
        output = ::print,
        onStart = { "\n${titledSplitter("generate started at $startTime")}\n" },
        onComplete = {
            val completeTime = Clock.System.now()
            "\n${titledSplitter("generate completed in ${completeTime - startTime} at $completeTime")}\n"
        }
    )
}