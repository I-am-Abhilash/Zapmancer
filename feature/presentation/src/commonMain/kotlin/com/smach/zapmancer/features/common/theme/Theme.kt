package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ZapTeal,
    onPrimary = Color.Black,

    secondary = ZapCoral,
    onSecondary = Color.Black,

    background = ZapDarkBg,
    onBackground = ZapDarkOnSurface,

    surface = ZapDarkSurface,
    onSurface = ZapDarkOnSurface,

    surfaceVariant = ZapDarkSurfaceVariant,
    onSurfaceVariant = ZapDarkOnSurfaceVariant,

    outline = ZapDarkOutline,

    error = ErrorRed
)
private val LightColorScheme = lightColorScheme(
    primary = ZapTeal,
    onPrimary = Color.White,

    secondary = ZapCoral,
    onSecondary = Color.White,

    background = ZapBg,
    onBackground = ZapOnSurface,

    surface = ZapSurface,
    onSurface = ZapOnSurface,

    surfaceVariant = ZapCream,
    onSurfaceVariant = ZapOnSurfaceVariant,

    outline = ZapOutline,
    error = ErrorRed,
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
