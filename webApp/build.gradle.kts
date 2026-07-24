plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
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
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
            implementation(npm("sql.js", "1.8.0"))

            implementation(libs.sqldelight.webworker)
            implementation(projects.core)

        }
    }
}
