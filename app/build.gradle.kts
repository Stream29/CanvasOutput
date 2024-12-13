plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":model"))
    implementation(libs.bundles.langchain4kt.qwen)
}

application {
    mainClass = "io.github.stream29.app.AppKt"
}
