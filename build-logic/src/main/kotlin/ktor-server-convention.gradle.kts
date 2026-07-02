plugins {
    id("org.jetbrains.kotlin.jvm")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

group = "com.smach.scriptside"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.findLibrary("ktor-server-core").get())
    implementation(libs.findLibrary("ktor-server-auth").get())
    implementation(libs.findLibrary("ktor-server-netty").get())
    implementation(libs.findLibrary("ktor-server-config-yaml").get())
    implementation(libs.findLibrary("logback-classic").get())
    testImplementation(libs.findLibrary("ktor-server-test-host").get())
    testImplementation(libs.findLibrary("kotlin-test-junit").get())
}
