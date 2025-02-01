package io.github.stream29.agent

import org.jetbrains.kotlin.mainKts.MainKtsScript
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

val host = BasicJvmScriptingHost()

fun eval(
    sourceCode: String,
) = captureOutput {
    println("Script output:")
    host.evalWithTemplate<MainKtsScript>(
        sourceCode.toScriptSource(),
        compileConfig,
        evaluationConfig
    ).onSuccess {
        when (val returnValue = it.returnValue) {
            is ResultValue.Unit -> println("return Unit")
            is ResultValue.Error -> println("throws: ${returnValue.error.stackTraceToString()}\nwrapped: ${returnValue.wrappingException?.stackTraceToString()}")
            ResultValue.NotEvaluated -> println("not evaluated")
            is ResultValue.Value -> println("return: ${returnValue.value}")
        }
        ResultWithDiagnostics.Success(it)
    }.onFailure {
        println("Script failed to execute")
        println("Errors: ${it.reports.joinToString("\n") { it.render(withStackTrace = true) }}")
    }
}


fun captureOutput(block: () -> Unit): String {
    val outStream = ByteArrayOutputStream()
    val prevOut = System.out
    System.setOut(PrintStream(outStream))
    try {
        block()
    } finally {
        System.out.flush()
        System.setOut(prevOut)
    }
    return outStream.toString()
}

val compileConfig: ScriptCompilationConfiguration.Builder.() -> Unit =
    {
        jvm {
            dependenciesFromClassContext(
                MainKtsScript::class,
                wholeClasspath = true
            )
        }
    }

val evaluationConfig: ScriptEvaluationConfiguration.Builder.() -> Unit =
    {
        jvm {
            baseClassLoader(Thread.currentThread().contextClassLoader)
        }
        constructorArgs(emptyArray<String>())
        enableScriptsInstancesSharing()
    }