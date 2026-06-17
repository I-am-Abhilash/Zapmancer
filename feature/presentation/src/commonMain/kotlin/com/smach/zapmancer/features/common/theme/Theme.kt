package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = Indigo200,
        onPrimary = Indigo900,
        primaryContainer = Indigo700,
        onPrimaryContainer = Indigo100,
        secondary = IndigoAccent200,
        onSecondary = Color.White,
        secondaryContainer = Indigo800,
        onSecondaryContainer = IndigoAccent100,
        background = Ink900,
        onBackground = Ink100,
        surface = Ink800,
        onSurface = Ink100,
        surfaceVariant = Ink700,
        onSurfaceVariant = Ink300,
        error = ErrorRed,
        onError = Color.White,
        outline = Ink500,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Indigo600,
        onPrimary = Color.White,
        primaryContainer = Indigo100,
        onPrimaryContainer = Indigo900,
        secondary = IndigoAccent400,
        onSecondary = Color.White,
        secondaryContainer = Indigo50,
        onSecondaryContainer = IndigoAccent700,
        background = Color.White,
        onBackground = Ink900,
        surface = Ink50,
        onSurface = Ink900,
        surfaceVariant = Ink200,
        onSurfaceVariant = Ink700,
        error = ErrorRed,
        onError = Color.White,
        outline = Ink400,
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
