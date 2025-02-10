package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.core.asRespondent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class QueryContext<HistoryPhases : ThoughtPhase<*>, ResponsePhases : HistoryPhases>(
    val chatApiProvider: ChatApiProvider<*>,
    val historyPhaseSerializer: KSerializer<HistoryPhases>,
    val responsePhaseSerializer: KSerializer<ResponsePhases>,
    val systemInstruction: String,
    val promptGen: QueryContext<HistoryPhases, ResponsePhases>.() -> String,
    val status: Status<HistoryPhases>,
    val parentTraceId: String = "",
    @Volatile
    var finished: Boolean = false,
) {
    val traceId: String = "$parentTraceId-${status.question}"
    val statusSerializer = Status.serializer(historyPhaseSerializer)
    val respondent = chatApiProvider.asRespondent(systemInstruction)
}

val <HistoryPhases : ThoughtPhase<*>, ResponsePhases : HistoryPhases>
        QueryContext<HistoryPhases, ResponsePhases>.prompt
    get() = promptGen()
val <HistoryPhases : ThoughtPhase<*>> QueryContext<HistoryPhases, *>.statusJson: String
    get() = json.encodeToString(statusSerializer, status)

@Serializable
@SerialName("History")
data class Status<HistoryPhases>(
    @Description("你的编辑历史")
    val history: MutableList<HistoryPhases> = mutableListOf(),
    @Description("你需要回复的问题")
    var question: String = "",
    @Description("你的回复，可以一遍思考一边编辑。请将答案填写在这张答题卡中")
    var responseCanvas: String = ""
)

