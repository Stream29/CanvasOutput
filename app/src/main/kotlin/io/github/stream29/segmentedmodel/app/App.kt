package io.github.stream29.segmentedmodel.app

import io.github.stream29.langchain4kt.core.asRespondent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val respondent = qwenChatApiProvider.asRespondent(systemInstruction)
        launch {
            respondent.chat("请分析两个物体碰撞的不同情况。")
        }
        launch {
            respondent.chat("请分析三个物体碰撞的不同情况。")
        }
    }
}
