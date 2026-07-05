plugins {
    id("ktor-server-convention")
    id("database-convention")
    alias(libs.plugins.ktor)
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}


application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":server-features"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
}

configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("androidx.savedstate:savedstate-compose-desktop"))
            .using(module("org.jetbrains.androidx.savedstate:savedstate-compose-desktop:${libs.versions.savedstateCompose.get()}"))
        substitute(module("androidx.savedstate:savedstate-compose"))
            .using(module("org.jetbrains.androidx.savedstate:savedstate-compose:${libs.versions.savedstateCompose.get()}"))
    }
}

tasks.shadowJar {
    isZip64 = true
}