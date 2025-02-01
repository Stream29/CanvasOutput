package io.github.stream29.segmentedmodel.app

import io.github.stream29.langchain4kt.core.asRespondent
import io.github.stream29.langchain4kt.core.generateFrom
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString

fun main() {
    runBlocking {
        val respondent = qwenChatApiProvider.asRespondent(systemInstruction<ThoughtPhase>())

        val prompt = BeginningPhase("现在时间是几点？")
        val history = ThoughtHistory(mutableListOf(prompt))
        while(true) {
            withRetry(5) {
                val phase = respondent.chat<ThoughtPhase>(json.encodeToString(history))
                if(phase is SummaryPhase) {
                    println(phase.summary)
                    return@runBlocking
                }
                history.phases.add(phase)
                if(phase is ScriptingPhase) {
                    val scriptingResultPhase = ScriptingResultPhase(eval(phase.script))
                    println(scriptingResultPhase.result)
                    history.phases.add(scriptingResultPhase)
                }
            }
        }
    }
}
