# Agent

核心思想：子任务拆解与文本引用

## 文本引用

agent可以通过action向StringPool中添加文本资源。
StringPool的本质是一个Map<String, String> title -> stringResource。
对于较短的文本，允许直接使用字符串添加，对于较长的文本，应当使用title+from+to的方式引用，应当允许不同来源的文本互相拼接。

```kotlin
data class AddStringSource(
    val title: String,
    val components: List<StringComponent>
)

interface StringComponent

data class StringValueComponent(
    val content: String
) : StringComponent

data class StringReferenceComponent(
    val title: String,
    val from: String,
    val to: String
) : StringComponent
```

通过这种方式，我们尽量通过文本引用来减轻上下文的token消耗和生成耗时。
当模型需要从StringPool加载文本时，可以通过执行一个action，将文本加载到自己的ContextWindow中。
当然，也应该允许模型从ContextWindow移除StringResource

```kotlin
data class LoadStringSource(
    val title: String
)

data class RemoveStringSource(
    val title: String
)
```

## 子任务拆解

理想的子任务应当有完全的并行执行、并行推理的能力。
为了实现这一点，我们希望不管是子任务还是父任务，都有着类似的执行方式。
所有的入参都应该指向一个StringResource，且所有的返回值也都应该指向一个StringResource。
一个task的执行，入参应当为一个单一的question，在这个question中包含我们所有的上下文信息。
对于task调用栈的实现，我认为目前只需要记录每一层调用栈的question即可。
初始情况下，我们的ContextWindow中应当只包含callStack+question，task启动后由模型自主加载StringResource。

```kotlin
data class TaskContextWindow(
    val history: List<TaskPhase>,
    val localStringPool: MutableMap<String, String>,
    val callStack: List<String>,
    val question: String,
    var canvas: String
)
```

## 改进的Phase

在此前，我们采取agent的纯自主Phase选择，这被证明随着模型的智力下降而可行性严重下降。

为了合理减少output schema以缩短system instruction，我认为完全可以采取一种分类式phase，
将phase分为三类，或者称为lifecycle：
- reasoning（纯思考），
- editing（纯编辑canvas），
- returning（新建一个StringResource作为返回值）。