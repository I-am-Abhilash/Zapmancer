plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.koin)
    alias(libs.plugins.kotlinSerialization)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
    openApi {
        enabled = true
        codeInferenceEnabled = true
        onlyCommented = false
    }
}

tasks.shadowJar {
    isZip64 = true
}

dependencies {
    implementation(files("libs/ktor-server-scalar-jvm-3.6.0-SNAPSHOT.jar"))

    implementation(project(":core"))
    implementation(libs.bcrypt)
    implementation(libs.koin.ktor)
    implementation(libs.koin.annotations)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.websockets)
    implementation(libs.kotlinx.datetime)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.request.validation)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.ktor.server.netty)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.jsoup)
    implementation(libs.commonmark)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.google.cloud.storage)
    implementation(libs.aws.s3)
    // Database-convention libraries
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.jedis)
}

configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("androidx.savedstate:savedstate-compose-desktop"))
            .using(module("org.jetbrains.androidx.savedstate:savedstate-compose-desktop:${libs.versions.savedstateCompose.get()}"))
        substitute(module("androidx.savedstate:savedstate-compose"))
            .using(module("org.jetbrains.androidx.savedstate:savedstate-compose:${libs.versions.savedstateCompose.get()}"))
    }
}
