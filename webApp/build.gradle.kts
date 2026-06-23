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
            implementation(libs.sqldelight.webworker)
        }
    }
}

val webpackConfigDir = project.file("webpack.config.d")

tasks.register("generateWebpackPolyfills") {
    description = "To generate the webpack missing Pollyfills"
    doLast {
        webpackConfigDir.mkdirs()
        val polyfillFile = File(webpackConfigDir, "polyfills.js")
        polyfillFile.writeText("""
            config.resolve = config.resolve || {};
            config.resolve.fallback = config.resolve.fallback || {};
            config.resolve.fallback.os = false;
            config.resolve.fallback.path = false;
            config.resolve.fallback.fs = false;
        """.trimIndent())
    }
}

tasks.matching { it.name.contains("Webpack", ignoreCase = true) }.configureEach {
    dependsOn("generateWebpackPolyfills")
}