plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    android {
        namespace = "com.smach.zapmancer.core"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "common"
            isStatic = true
        }
    }

    js {
        browser {
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.jetbrains.components.resources)
            implementation(libs.compose.uiTooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.napier)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.okio)
            implementation(libs.coil.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.koin.core)
            implementation(libs.navigation3.ui)
            implementation(libs.savedstateCompose)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.async.extensions1)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.ktor.client.mock)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.server.auth.jwt)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.request.validation)
            implementation(libs.ktor.server.rate.limit)
            implementation(libs.ktor.server.swagger)
            implementation(libs.ktor.server.cors)
            implementation(libs.ktor.server.status.pages)
            implementation(libs.ktor.server.openapi)
            implementation(libs.ktor.server.call.logging)
            implementation(libs.ktor.server.call.id)
            implementation(libs.ktor.server.metrics.micrometer)
            implementation(libs.micrometer.registry.prometheus)
            implementation(libs.jsoup)
            implementation(libs.commonmark)
            implementation(libs.kotlinx.datetime)
            api(libs.koin.ktor)
            implementation(libs.koin.logger.slf4j)
            implementation(libs.google.cloud.storage)
            implementation(libs.aws.s3)
            
            // Database-convention libraries
            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.kotlin.datetime)
            implementation(libs.hikaricp)
            implementation(libs.postgresql)
            implementation(libs.flyway.core)
            implementation(libs.flyway.database.postgresql)
        }

        val webSourceDir = "src/webMain/kotlin"

        webMain.get().apply {
            kotlin.srcDirs(webSourceDir)
            dependencies {
                implementation(libs.sqldelight.webworker)
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.smach.zapmancer.core.database")
            generateAsync.set(true)
        }
    }
}
