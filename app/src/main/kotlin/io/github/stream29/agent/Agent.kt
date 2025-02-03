package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.core.asRespondent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

interface Agent {
    val status: Status
    var requestSuspend: Boolean
    suspend fun query(input: String): String
}

@Serializable
@SerialName("History")
data class Status(
    @Description("你的编辑历史")
    val history: MutableList<ThoughtPhase> = mutableListOf(),
    @Description("你需要回复的问题")
    var question: String = "",
    @Description("你的回复，可以一遍思考一边编辑")
    var responseCanvas: String = ""
)

@Serializable
sealed interface ThoughtPhase {
    suspend fun joinTo(agent: Agent) {
        agent.status.history.add(this)
    }
}

data class SimpleAgent(
    val chatApiProvider: ChatApiProvider<*>,
    override val status: Status
) : Agent {
    @Volatile
    override var requestSuspend = false
    val respondent = chatApiProvider.asRespondent(systemInstruction<ThoughtPhase>())
    override suspend fun query(input: String): String {
        status.question = input
        while (true) {
            withRetry(5) {
                val phase = respondent.chat<ThoughtPhase>(
                    json.encodeToString(status) +
                            "\n请基于以上的history，继续编辑canvas或停下来思考以生成对question的回复"
                )
                phase.joinTo(this)
                println(json.encodeToString(phase))
                println(status.responseCanvas)
                if (requestSuspend) return status.responseCanvas
            }
        }
    }
}