package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Coinbase Design System — Theme ───────────────────────────────────────────
// Source: CoinBaseDESIGN.md / Coinbase-design-analysis
//
// Light: Pure white canvas, Coinbase Blue primary CTAs, hairline borders.
// Dark:  Deep near-black background, graphite cards, Coinbase Blue CTAs.
//
// All UI uses MaterialTheme.colorScheme.* tokens for absolute centralization.

// ─── Light ────────────────────────────────────────────────────────────────────
private val CoinbaseLightColorScheme = lightColorScheme(
    primary             = CoinbaseBlue,         // Coinbase Blue (#0052FF)
    onPrimary           = CoinbaseOnPrimary,    // White (#FFFFFF)

    primaryContainer    = CoinbaseBlue,
    onPrimaryContainer  = CoinbaseOnPrimary,

    secondary           = CoinbaseSurfaceStrong, // Soft gray fill (#EEF0F3) for secondary CTAs/chips
    onSecondary         = CoinbaseInk,          // Deep near-black text

    secondaryContainer  = CoinbaseSurfaceStrong,
    onSecondaryContainer= CoinbaseInk,

    tertiary            = CoinbaseYellow,       // Sparse brand illustration yellow (perfect for stars)
    onTertiary          = CoinbaseInk,

    tertiaryContainer   = CoinbaseSurfaceStrong,
    onTertiaryContainer = CoinbaseInk,

    background          = CoinbaseCanvas,       // Pure White (#FFFFFF)
    onBackground        = CoinbaseInk,          // Deep near-black (#0A0B0D)

    surface             = CoinbaseCanvas,
    onSurface           = CoinbaseInk,

    surfaceVariant      = CoinbaseSurfaceSoft,  // Very light grey (#F7F7F7) for input backgrounds
    onSurfaceVariant    = CoinbaseBody,         // Cool grey (#5B616E) for running text

    outline             = CoinbaseHairline,     // 1px hairline border (#DEE1E6)
    outlineVariant      = CoinbaseHairlineSoft, // Lighter hairline (#EEF0F3)

    inverseSurface      = CoinbaseInk,
    inverseOnSurface    = CoinbaseCanvas,
    inversePrimary      = CoinbaseOnPrimary,

    error               = CoinbaseDown,         // Price down / semantic red (#CF202F)
    onError             = CoinbaseOnPrimary,
    errorContainer      = Color(0xFFFFDAD6),
    onErrorContainer    = CoinbaseDown,

    scrim               = CoinbaseInk,
    surfaceTint         = Color.Transparent,
)

// ─── Dark ─────────────────────────────────────────────────────────────────────
private val CoinbaseDarkColorScheme = darkColorScheme(
    primary             = CoinbaseBlue,         // Blue remains the main action color on dark
    onPrimary           = CoinbaseOnPrimary,

    primaryContainer    = CoinbaseDarkSurface,
    onPrimaryContainer  = CoinbaseDarkOnSurface,

    secondary           = CoinbaseDarkSurfaceTop, // input container, chip bg
    onSecondary         = CoinbaseDarkOnSurface,

    secondaryContainer  = CoinbaseDarkSurfaceMid,
    onSecondaryContainer= CoinbaseDarkOnMuted,

    tertiary            = CoinbaseYellow,
    onTertiary          = CoinbaseDarkBase,

    tertiaryContainer   = CoinbaseDarkSurface,
    onTertiaryContainer = CoinbaseDarkOnSurface,

    background          = CoinbaseDarkBase,      // Deep near-black (#0A0B0D)
    onBackground        = CoinbaseDarkOnSurface, // White (#FFFFFF)

    surface             = CoinbaseDarkSurface,   // Graphite card surface (#16181C)
    onSurface           = CoinbaseDarkOnSurface,

    surfaceVariant      = CoinbaseDarkSurfaceTop, // #272B33
    onSurfaceVariant    = CoinbaseDarkOnMuted,   // Muted off-white (#A8ACB3)

    outline             = CoinbaseDarkHairline,  // Divider/hairlines on dark (#2C2F38)
    outlineVariant      = CoinbaseDarkHairline,

    inverseSurface      = CoinbaseCanvas,
    inverseOnSurface    = CoinbaseInk,
    inversePrimary      = CoinbaseBlue,

    error               = CoinbaseDarkDown,      // Brightened semantic red for dark WCAG contrast (#FF4D58)
    onError             = CoinbaseDarkBase,
    errorContainer      = Color(0xFF5C0A0A),
    onErrorContainer    = Color(0xFFFFB4AB),

    scrim               = CoinbaseDarkBase,
    surfaceTint         = Color.Transparent,
)

// ─── AppTheme ─────────────────────────────────────────────────────────────────

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) CoinbaseDarkColorScheme else CoinbaseLightColorScheme,
        typography  = Typography,
        shapes      = Shapes,
        content     = content,
    )
}
