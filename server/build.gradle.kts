plugins {
    id("ktor-server-convention")
    id("database-convention")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}

tasks.shadowJar {
    isZip64 = true
}


dependencies {
    implementation(project(":core"))

    implementation(libs.bcrypt)
    implementation(libs.koin.ktor)
    implementation(libs.ktor.server.rate.limit)
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
