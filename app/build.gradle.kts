plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(project(":model"))
    implementation(libs.bundles.langchain4kt.qwen)
    implementation(libs.bundles.langchain4kt.framework)
}

application {
    mainClass = "io.github.stream29.app.AppKt"
}
