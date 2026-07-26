// package com.smach.zapmancer.presentation.common.adaptive
//
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.staticCompositionLocalOf
//
// /**
// * Three-step window layout class used across the app.
// *
// *  - [Compact]: phones in portrait (<600dp). Use bottom navigation,
// *    single-column layouts, full-bleed content.
// *  - [Medium]:  small tablets, phones in landscape, foldables
// *    (600–840dp). Use navigation rail, two-column where appropriate,
// *    constrained content width.
// *  - [Expanded]: large tablets, desktop, web (>840dp). Use permanent
// *    navigation drawer, multi-column layouts, max content width.
// */
// enum class WindowLayout {
//    Compact,
//    Medium,
//    Expanded,
//    ;
//
//    val isCompact: Boolean get() = this == Compact
//    val isMedium: Boolean get() = this == Medium
//    val isExpanded: Boolean get() = this == Expanded
//    val isAtLeastMedium: Boolean get() = this != Compact
//
//    val contentMaxWidthDp: Int
//        get() = when (this) {
//            Compact -> Int.MAX_VALUE
//            Medium -> 720
//            Expanded -> 1200
//        }
//
//    val screenHorizontalPaddingDp: Int
//        get() = when (this) {
//            Compact -> 16
//            Medium -> 32
//            Expanded -> 48
//        }
// }
//
// @Composable
// fun rememberWindowLayout(): WindowLayout {
//    val info = androidx.compose.material3.adaptive.currentWindowAdaptiveInfo()
//    return when (info.windowSizeClass.windowWidthSizeClass) {
//        androidx.window.core.layout.WindowWidthSizeClass.COMPACT -> WindowLayout.Compact
//        androidx.window.core.layout.WindowWidthSizeClass.MEDIUM -> WindowLayout.Medium
//        androidx.window.core.layout.WindowWidthSizeClass.EXPANDED -> WindowLayout.Expanded
//        else -> WindowLayout.Compact
//    }
// }
//
// val LocalWindowLayout = staticCompositionLocalOf { WindowLayout.Compact }
