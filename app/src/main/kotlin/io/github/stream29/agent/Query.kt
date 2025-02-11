package io.github.stream29.agent

import io.github.stream29.langchain4kt.core.ChatApiProvider
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
inline fun
        <reified HistoryPhases : ThoughtPhase<*>, reified ResponsePhases : HistoryPhases>
        buildQuery(block: QueryBuilder<HistoryPhases, ResponsePhases>.() -> Unit) =
    QueryBuilder<HistoryPhases, ResponsePhases>()
        .apply {
            historyPhaseSerializer = serializer<HistoryPhases>()
            responsePhaseSerializer = serializer<ResponsePhases>()
        }.apply(block)
        .run {
            QueryContext(
                chatApiProvider = chatApiProvider,
                systemInstruction = systemInstruction,
                promptGen = prompt,
                status = Status(
                    history = history,
                    question = question,
                    responseCanvas = responseCanvas
                ),
                parentTraceId = parentTraceId,
                finished = false,
                historyPhaseSerializer = historyPhaseSerializer,
                responsePhaseSerializer = responsePhaseSerializer,
            )
        }

suspend inline fun
        <reified HistoryPhases : ThoughtPhase<Bound>, reified ResponsePhases : HistoryPhases, Bound>
        QueryContext<HistoryPhases, ResponsePhases>.query(): String {
    while (true) {
        withRetry(5) {
            val phase = json.decodeFromString(
                responsePhaseSerializer,
                respondent.chat(prompt).normalizedJsonOutput()
            )
            @Suppress("unchecked_cast")
            phase.joinTo(this as QueryContext<Bound, *>)
            if (finished)
                return status.responseCanvas
        }
    }
}

class QueryBuilder<HistoryPhases : ThoughtPhase<*>, ResponsePhases : HistoryPhases> {
    lateinit var chatApiProvider: ChatApiProvider<*>
    lateinit var systemInstruction: String
    lateinit var historyPhaseSerializer: KSerializer<HistoryPhases>
    lateinit var responsePhaseSerializer: KSerializer<ResponsePhases>
    var prompt: QueryContext<HistoryPhases, ResponsePhases>.() -> String = defaultPrompt
    var history: MutableList<HistoryPhases> = mutableListOf()
    lateinit var question: String
    var responseCanvas: String = ""
    var parentTraceId: String = ""
}

inline val <HistoryPhases : ThoughtPhase<*>, ResponsePhases : HistoryPhases>
        QueryBuilder<HistoryPhases, ResponsePhases>.defaultPrompt: QueryContext<HistoryPhases, ResponsePhases>.() -> String
    get() = { "$statusJson\n请基于以上的history，继续编辑responseCanvas或停下来思考以生成对question的回复。你必须忠实地执行question的要求，并且检查执行是否无误" }

inline val <reified ResponsePhases> QueryBuilder<*, ResponsePhases>.defaultSystemInstruction
    get() = """
${schemaOf<ResponsePhases>()}
以上为一个json schema。你的输出必须符合这个schema。
注意，你应当合理处理转义符号，使得你的输出符合JSON规范。
禁止使用markdown格式。
""".trimIndent()