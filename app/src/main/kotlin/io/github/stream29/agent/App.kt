package io.github.stream29.agent

import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val simpleAgent = SimpleAgent(qwenChatApiProvider, Status())
        simpleAgent.query("请介绍一下中国的MC玩家群体")
    }
}
