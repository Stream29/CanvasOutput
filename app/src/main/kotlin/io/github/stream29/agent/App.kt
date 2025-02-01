package io.github.stream29.agent

import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val simpleAgent = SimpleAgent(qwenChatApiProvider, loggingMutableListOf())
        simpleAgent.query("计算斐波那契数列的114514项比1919810项大多少个数量级")
    }
}
