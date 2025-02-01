package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.jsonschemagenerator.RefWithSerialName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Outline")
@RefWithSerialName
@Description("对接下来的编辑进行规划")
data class OutlinePhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("逐条列出大纲")
    val outline: List<OutlineColumn>,
) : ThoughtPhase

@Serializable
@SerialName("OutlineColumn")
@RefWithSerialName
@Description("逐层深入的树状大纲结构")
data class OutlineColumn(
    @Description("这一节的内容")
    val content: String,
    @Description("这一节的子条目")
    val children: List<OutlineColumn> = emptyList(),
)

@Serializable
@SerialName("Reflection")
@RefWithSerialName
@Description("对目前的编辑进行反思，思考是否有不足或可以提升的地方。在Finish之前至少要有一次Reflection")
data class ReflectionPhase(
    val columns: List<String>
) : ThoughtPhase

@Serializable
@SerialName("Append")
@RefWithSerialName
@Description("思考之后在canvas的最后面插入内容")
data class AppendPhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("要插入的内容")
    val content: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        super.joinTo(agent)
        with(agent.status) {
            responseCanvas = responseCanvas + content
        }
    }
}

@Serializable
@SerialName("Insert")
@RefWithSerialName
@Description("思考之后更新canvas的内容")
data class InsertPhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("在canvas中这段文字第一次出现的位置之后插入内容")
    val insertAfter: String,
    @Description("要插入的内容")
    val content: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        super.joinTo(agent)
        with(agent.status) {
            val insertIndex = (responseCanvas.indexOf(insertAfter) + insertAfter.length)
                .coerceIn(0..responseCanvas.length)
            responseCanvas = responseCanvas.substring(0, insertIndex) + content + responseCanvas.substring(insertIndex)
        }
    }
}

@Serializable
@SerialName("Replace")
@RefWithSerialName
@Description("思考之后更新canvas的内容")
data class ReplacePhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("要替换掉的内容，替换位置为第一次出现的位置")
    val from: String,
    @Description("替换后的内容")
    val to: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        super.joinTo(agent)
        with(agent.status) {
            responseCanvas = responseCanvas.replaceFirst(from, to)
        }
    }
}

@Serializable
@SerialName("Finish")
@RefWithSerialName
@Description("结束对canvas的编辑")
data class FinishPhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        agent.requestSuspend = true
    }
}

inline fun <reified T> systemInstruction() = """
${schemaOf<T>()}
以上为一个json schema。你的输出必须符合这个schema。
输入的内容是一段思考历史，包含若干个phase，你应当基于思考历史继续思考，只需要输出一个phase。
注意，你应当合理处理转义符号，使得你的输出符合JSON规范。同时，你的输出应当以{开始，以}结束。
禁止使用markdown格式。
""".trimIndent()