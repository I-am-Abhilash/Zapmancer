package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//
//private val LightColorScheme = lightColorScheme(
//    primary = Teal700,
//    onPrimary = Color.White,
//
//    secondary = Coral500,
//    onSecondary = Color.White,
//
//    background = BackgroundLight,
//    onBackground = OnSurfaceLight,
//
//    surface = SurfaceLight,
//    onSurface = OnSurfaceLight,
//
//    surfaceVariant = SurfaceVariantLight,
//    onSurfaceVariant = OnSurfaceVariantLight,
//
//    outline = OutlineLight,
//
//    error = Error
//)
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Teal500,
//    onPrimary = Color.Black,
//
//    secondary = Coral500,
//    onSecondary = Color.Black,
//
//    background = BackgroundDark,
//    onBackground = OnSurfaceDark,
//
//    surface = SurfaceDark,
//    onSurface = OnSurfaceDark,
//
//    surfaceVariant = SurfaceVariantDark,
//    onSurfaceVariant = OnSurfaceVariantDark,
//
//    outline = OutlineDark,
//
//    error = Error
//)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,

    secondary = BlueSecondary,
    onSecondary = Color.White,


    background = BackgroundLight,
    onBackground = OnSurfaceLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,

    outline = OutlineLight,

    error = Error
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color.Black,

    secondary = BlueAccent,
    onSecondary = Color.Black,

    background = BackgroundDark,
    onBackground = OnSurfaceDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,

    outline = OutlineDark,

    error = Error
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
