import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                devServer = (devServer?: KotlinWebpackConfig.DevServer()).apply {
                    port = 3000
                }
                outputFileName = "webApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(libs.ui)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        jsMain.dependencies {
            implementation(projects.core)

        }
    }
}
