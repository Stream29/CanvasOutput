package io.github.stream29.agent

suspend inline fun withRetry(retryCount: Int, block: suspend () -> Unit) {
    var count = 0
    while (count < retryCount) {
        try {
            block()
            break
        } catch (e: Exception) {
            e.printStackTrace()
            count++
        }
    }
}