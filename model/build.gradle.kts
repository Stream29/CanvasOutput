plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.bundles.kotlinx.ecosystem)
    implementation(libs.bundles.langchain4kt.framework)
    testImplementation(kotlin("test"))
}