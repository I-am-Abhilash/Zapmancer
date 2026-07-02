plugins {
    id("ktor-server-convention")
    id("database-convention")
    alias(libs.plugins.kotlinSerialization)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bcrypt)
    implementation(libs.koin.ktor)
    implementation(libs.ktor.server.rate.limit)
}
