plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.mokkery) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless) apply false
}

// detekt {
//     toolVersion = libs.versions.detekt.get()
//     config.setFrom(files("$rootDir/detekt/detekt.yml"))
//     buildUponDefaultConfig = true
// }
// subprojects {

//     apply(plugin = "io.gitlab.arturbosch.detekt")

//     extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {

//         toolVersion = "1.23.8"

//         config.setFrom(
//             files("$rootDir/detekt/detekt.yml")
//         )

//         buildUponDefaultConfig = true
//     }
// }

// subprojects {

//     apply(plugin = "com.diffplug.spotless")

//     configure<com.diffplug.gradle.spotless.SpotlessExtension> {

//         kotlin {

//             target("**/*.kt")

//             targetExclude("**/build/**/*.kt")
//             targetExclude("bin/**/*.kt")

//             ktlint().editorConfigOverride(
//                 mapOf(
//                     "disabled_rules" to "filename",
//                     "ktlint_standard_function-naming" to "disabled",
//                     "ktlint_standard_filename" to "disabled",
//                     "ktlint_standard_kdoc" to "disabled"
//                 )
//             )
//         }

//         kotlinGradle {

//             target("**/*.gradle.kts")

//             ktlint()
//         }
//     }
// }

