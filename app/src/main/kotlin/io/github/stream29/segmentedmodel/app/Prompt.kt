package io.github.stream29.segmentedmodel.app

import io.github.stream29.jsonschemagenerator.Description
import io.github.stream29.jsonschemagenerator.toSchema
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
@Description("你的一段思考")
data class ThoughtSegment(
    @Description("你思考内容的标题")
    val title: String,
    @Description("你需要解决的问题")
    val question: String,
    @Description("你思考的过程，一条一条展示")
    val thinks: List<String>,
    @Description("你的思考结果")
    val result: String,
)

val systemInstruction = """
${json.encodeToString(ThoughtSegment.serializer().descriptor.toSchema())}
以上为一个json schema。你的输出必须符合这个schema。
你的输出内容必须为纯json，不可以包含任何其他内容。
""".trimIndent()