plugins {
    id("ktor-server-convention")
    alias(libs.plugins.ktor)
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

tasks.shadowJar {
    isZip64 = true
}
