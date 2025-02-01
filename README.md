# Agent

背后的这套工具链差不多都是我自己搓出来的，很神奇的是，这套完整的工具链真的可以用来开发agent！

基于langchain4kt的大模型调用。

基于kotlinx.serialization与json-schema-generator的支持，实现了LLM的结构化输入/输出。

然后通过kotlinx.serialization支持的多态序列化，模型可以每次进行一个phase的思考。

如果是模型需要调用main.kts脚本，我们也可以允许它调用。

这样的话，约等于是实现了一个半双工的通讯，每次模型进行一个phase的思考，我们都可以基于它返回的这个phase进行一些操作。
