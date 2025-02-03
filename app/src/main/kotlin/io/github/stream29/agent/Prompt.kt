package io.github.stream29.agent

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.jsonschemagenerator.RefWithSerialName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Outline")
@RefWithSerialName
@Description("对接下来的编辑进行规划，允许在已经进行一部分编辑以后对已有的大纲进行调整")
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
@SerialName("Reasoning")
@RefWithSerialName
@Description("在编辑的过程中，对目前的思路进行整理和推理，以得到更优质的输出")
data class ReasoningPhase(
    @Description("可能很长的推理步骤，越长越仔细越好")
    val steps: List<String>
) : ThoughtPhase

@Serializable
@SerialName("Reflection")
@RefWithSerialName
@Description("在编辑的过程中，对目前的编辑进行反思，思考是否有不足或可以提升的地方。在Finish之前至少要有一次Reflection")
data class ReflectionPhase(
    val columns: List<String>
) : ThoughtPhase

@Serializable
@SerialName("Append")
@RefWithSerialName
@Description("在canvas的后面添加内容，如果要在中间添加，应当使用Insert")
data class AppendPhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("要添加的内容")
    val content: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        with(agent.status) {
            responseCanvas = responseCanvas + content
        }
        super.joinTo(agent)
    }
}

@Serializable
@SerialName("Insert")
@RefWithSerialName
@Description("向canvas中的某个位置插入内容，如果canvas为空，应当使用Append")
data class InsertPhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("在canvas中这段文字出现的位置之后插入内容，这里的字符串必须在canvas中出现且仅出现过一次")
    val insertAfter: String,
    @Description("要插入的内容")
    val content: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        with(agent.status) {
            val insertIndex = (responseCanvas.indexOf(insertAfter) + insertAfter.length)
            if (insertIndex - insertAfter.length == -1)
                throw RuntimeException("$insertAfter not found in $responseCanvas")
            responseCanvas = responseCanvas.substring(0, insertIndex) + content + responseCanvas.substring(insertIndex)
        }
        super.joinTo(agent)
    }
}

@Serializable
@SerialName("Replace")
@RefWithSerialName
@Description("思考之后修改canvas中已有的内容")
data class ReplacePhase(
    @Description("你的编辑计划以及你的编辑理由")
    val pre: String,
    @Description("要替换掉的内容，替换位置为第一次出现的位置")
    val from: String,
    @Description("替换后的内容")
    val to: String,
) : ThoughtPhase {
    override suspend fun joinTo(agent: Agent) {
        with(agent.status) {
            responseCanvas = responseCanvas.replaceFirst(from, to)
        }
        super.joinTo(agent)
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
注意，你应当合理处理转义符号，使得你的输出符合JSON规范。同时，你的输出应当以{开始，以}结束。
禁止使用markdown格式。
""".trimIndent()