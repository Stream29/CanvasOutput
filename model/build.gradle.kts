plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.bundles.kotlinx.ecosystem)
    testImplementation(kotlin("test"))
}