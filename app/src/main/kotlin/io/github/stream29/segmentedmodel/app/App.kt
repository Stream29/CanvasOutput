package io.github.stream29.segmentedmodel.app

import io.github.stream29.langchain4kt.core.asRespondent
import io.github.stream29.langchain4kt.core.generateFrom
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString

fun main() {
    runBlocking {
        val respondent = qwenChatApiProvider.asRespondent(systemInstruction<ThoughtPhases>())
        launch {
            val phases = respondent.chat<ThoughtPhases>("草莓的英文单词中含有几个r？")
            qwenChatApiProvider.generateFrom("""
                ${json.encodeToString(phases)}
                请基于以上的思考过程，生成一个符合要求的回复。
            """.trimIndent())
        }
    }
}
