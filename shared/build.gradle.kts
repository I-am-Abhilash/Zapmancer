import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidLibrary {
        namespace = "com.smach.zapmancer.shared"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.name
            isStatic = true
        }
    }

    js {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.feature.data)
            implementation(projects.feature.domain)
            implementation(projects.feature.presentation)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.material.icons.extended)
            implementation(libs.navigation3.ui)
            implementation(libs.savedstateCompose)

            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.jetbrains.components.resources)
            implementation(libs.compose.uiTooling.preview)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
buildkonfig {
    packageName = "com.smach.zapmancer"
    objectName = "AppConfig"

    defaultConfigs {
//        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "https://api.scriptside.com/")
        buildConfigField(FieldSpec.Type.BOOLEAN, "IS_DEBUG", "false")
    }

    defaultConfigs("dev") {
//        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "https://dev-api.scriptside.com/")
        buildConfigField(FieldSpec.Type.BOOLEAN, "IS_DEBUG", "true")
    }
}
