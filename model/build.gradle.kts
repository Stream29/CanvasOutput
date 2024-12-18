plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    api(libs.bundles.kotlinx.ecosystem)
    api(libs.bundles.langchain4kt.framework)
    api(libs.json.schema.generator)
    testImplementation(kotlin("test"))
}