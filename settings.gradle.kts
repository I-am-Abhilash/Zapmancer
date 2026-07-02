rootProject.name = "Zapmancer"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

// Client Multiplatform Modules
include(":webApp")
include(":androidApp")
include(":shared")
include(":core")
include(":feature:data")
include(":feature:domain")
include(":feature:presentation")

// Server Ktor Modules
include(":server-app")
project(":server-app").projectDir = file("server/app")

include(":server-features")
project(":server-features").projectDir = file("server/features")
