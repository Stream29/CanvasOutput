package io.github.stream29.agent

import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.core.asRespondent
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

interface Agent {
    val history: MutableList<ThoughtPhase>
    suspend fun query(input: String): String
}

@Serializable
sealed interface ThoughtPhase {
    suspend fun joinTo(agent: Agent) {
        agent.history.add(this)
    }
}

data class SimpleAgent(
    val chatApiProvider: ChatApiProvider<*>,
    override val history: MutableList<ThoughtPhase>
) : Agent {
    val respondent = chatApiProvider.asRespondent(systemInstruction<ThoughtPhase>())
    override suspend fun query(input: String): String {
        history.add(BeginningPhase(input))
        while (true) {
            withRetry(5) {
                val phase = respondent.chat<ThoughtPhase>(json.encodeToString(history))
                phase.joinTo(this)
                if (phase is SummaryPhase)
                    return phase.summary
            }
        }
    }
}