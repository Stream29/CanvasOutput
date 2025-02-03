plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.kotlinx.serialization)
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(libs.bundles.kotlin.scripting)
    implementation(libs.json.schema.generator)
    implementation(libs.bundles.langchain4kt.api)
    implementation(libs.bundles.langchain4kt.framework)
}

application {
    mainClass = "io.github.stream29.agent.AppKt"
}
