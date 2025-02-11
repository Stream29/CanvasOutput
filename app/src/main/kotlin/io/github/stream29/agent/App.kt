package io.github.stream29.agent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    runBlocking(Dispatchers.IO) {
        val sourceSubtitle = File("app/src/main/resources/script.txt").readText()
        val result = buildQuery<BasicReasoningPhases, BasicReasoningPhases> {
            chatApiProvider = geminiChatApiProvider
            systemInstruction = defaultSystemInstruction
            prompt = defaultPrompt
            question = "$sourceSubtitle\n\n\n以上是一个视频的字幕，你应当按顺序做两件事：首先添加标点，将字幕不加修改地连接起来；然后在适当的位置插入空行，帮助分层"
        }.query()
        println(result)
        println("长度缩减情况：整理后的文本为原文的${result.length * 100 / sourceSubtitle.length}%")
    }
}

