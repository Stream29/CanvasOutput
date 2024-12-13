plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":model"))
}

application {
    mainClass = "io.github.stream29.app.AppKt"
}
