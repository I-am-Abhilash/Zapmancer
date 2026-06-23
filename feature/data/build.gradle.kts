plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.smach.zapmancer.data"

        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    js {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.feature.domain)
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}
