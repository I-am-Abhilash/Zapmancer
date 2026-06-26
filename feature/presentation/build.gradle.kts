plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    android {
        namespace = "com.smach.zapmancer.features"
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
            implementation(projects.feature.domain)
            implementation(projects.core)
            implementation(libs.runtime)
            implementation(libs.savedstateCompose)
            implementation(libs.lifecycleViewmodelCompose)
            implementation(libs.lifecycleRuntimeCompose)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.coil.compose)
            implementation(libs.jetbrains.components.resources)
            implementation(libs.material.icons.extended)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.adaptive.layout)
            implementation(libs.adaptive)
            implementation(libs.adaptive.navigation)
            implementation(libs.okio)
            implementation(libs.koin.core)
            implementation(libs.compose.uiTooling.preview)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiTooling)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.mokkery.runtime)
        }
    }
}
