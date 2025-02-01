package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.jsonschemagenerator.RefWithSerialName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThoughtHistory(
    @Description("你的思考各个阶段")
    val phases: MutableList<ThoughtPhase>,
)

@Serializable
@SerialName("Beginning")
@RefWithSerialName
data class BeginningPhase(
    @Description("开始思考的问题")
    val question: String,
) : ThoughtPhase

@Serializable
@SerialName("Reasoning")
@RefWithSerialName
data class ReasoningPhase(
    @Description("从Outline中选择一个问题开始思考")
    val question: String,
    @Description("思考的过程")
    val thoughts: String,
    @Description("思考的结论")
    val conclusion: String,
    @Description("对思考过程的检查")
    val check: String,
) : ThoughtPhase

@Serializable
@Suppress("unused")
@SerialName("Outline")
@RefWithSerialName
data class OutlinePhase(
    @Description("构思思考大纲之前的过程")
    val pre: String,
    @Description("思考大纲")
    val outline: List<OutlineColumn>,
) : ThoughtPhase

@Serializable
@SerialName("Summary")
@RefWithSerialName
data class SummaryPhase(
    @Description("基于思考的过程，给出对初始问题的答案")
    val summary: String,
) : ThoughtPhase

@Serializable
@SerialName("Kts")
@RefWithSerialName
@Description("当你需要执行main.kts脚本时，使用这个phase")
data class ScriptingPhase(
    @Description("一段main.kts脚本，将会被执行。不需要写main函数，脚本的最后一行会被当作返回值")
    val script: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        super.joinTo(agent)
        agent.history.add(ScriptingResultPhase(eval(script)))
    }
}

@Serializable
@SerialName("KtsResult")
@RefWithSerialName
data class ScriptingResultPhase(
    @Description("脚本的运行结果，包括输出和返回值")
    val result: String,
) : ThoughtPhase

@Serializable
@SerialName("OutlineColumn")
@RefWithSerialName
@Description("大纲中的一条")
data class OutlineColumn(
    @Description("这条大纲的内容")
    val content: String,
    @Description("这条大纲的子大纲")
    val child: List<OutlineColumn> = emptyList(),
)

inline fun <reified T> systemInstruction() = """
${schemaOf<T>()}
以上为一个json schema。你的输出必须符合这个schema。
输入的内容是一段思考历史，包含若干个phase，你应当基于思考历史继续思考，只需要输出一个phase。
注意，你应当合理处理转义符号，使得你的输出符合JSON规范。同时，你的输出应当以{开始，以}结束。
禁止使用markdown格式。
""".trimIndent()