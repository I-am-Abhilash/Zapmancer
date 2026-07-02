plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.flywaydb.flyway")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("exposed-core").get())
    implementation(libs.findLibrary("exposed-dao").get())
    implementation(libs.findLibrary("exposed-jdbc").get())
    implementation(libs.findLibrary("exposed-kotlin-datetime").get())
    implementation(libs.findLibrary("kotlinx-datetime").get())
    implementation(libs.findLibrary("hikaricp").get())
    implementation(libs.findLibrary("postgresql").get())
    implementation(libs.findLibrary("flyway-core").get())
    implementation(libs.findLibrary("flyway-database-postgresql").get())
}
