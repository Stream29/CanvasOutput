dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/Stream29/JsonSchemaGenerator")
            credentials {
                username = System.getenv("GITHUB_ACTOR")!!
                password = System.getenv("GITHUB_TOKEN")!!
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":app")
include(":model")

rootProject.name = "SegmentedModel"