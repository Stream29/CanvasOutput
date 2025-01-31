package io.github.stream29.segmentedmodel.app

import io.github.stream29.jsonschemagenerator.Description
import kotlinx.serialization.Serializable

@Serializable
data class ThoughtPhases(
    @Description("你的思考各个阶段")
    val phases: List<ThoughtPhase>,
)

@Serializable
@Description("你的思考上下文")
data class ThoughtPhase(
    @Description("你之前的思考历史")
    val history: String,
    @Description("初始需要解决的问题")
    val question: String,
    @Description("目前需要解决的问题")
    val current: String,
    @Description("你对目前问题思考的过程，一条一条展示")
    val thinks: List<String>,
    @Description("对历史思考的总结")
    val result: String,
    @Description("是否需要继续思考")
    val furtherNeeded: Boolean,
)

inline fun <reified T> systemInstruction() = """
${schemaOf<T>()}
以上为一个json schema。你的输出必须符合这个schema。
你的输出内容必须为纯json，不可以包含任何其他内容。
""".trimIndent()