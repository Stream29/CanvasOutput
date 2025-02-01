package io.github.stream29.agent

import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val simpleAgent = SimpleAgent(qwenChatApiProvider, Status())
        simpleAgent.query("请写一篇文言文的策论，分析人工智能当下的进展")
    }
}
