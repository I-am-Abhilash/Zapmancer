plugins {
    `kotlin-dsl`
}

group = "com.smach.scriptside.convention"

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlin-gradle-plugin").get())
    implementation(libs.findLibrary("ktor-gradle-plugin").get())
    implementation(libs.findLibrary("flyway-gradle-plugin").get())
}
