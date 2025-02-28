package io.github.stream29.agent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking(Dispatchers.IO) {
        val simpleAgent = SimpleAgent(qwenChatApiProvider, Status())
        val result = simpleAgent.query("从1开始，允许两种操作，+10和*1.5，如何以最少的步骤得到一个超过100的数？")
        println(result)
    }
}
