package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Nike Design System — Theme ────────────────────────────────────────────────
// Source: DESIGN.md / Nike-design-analysis
//
// Light: 95% chrome is ink/canvas/soft-cloud — editorial photography-first.
// Dark:  surfaces invert to warm graphite; CTAs flip to white-on-dark.
//
// Both schemes are 100% driven by Color.kt tokens.
// All component code uses MaterialTheme.colorScheme.* — no hardcoded colors.

// ─── Light ────────────────────────────────────────────────────────────────────
private val NikeLightColorScheme = lightColorScheme(
    primary = NikeInk, // #111111 — CTAs, active filters
    onPrimary = NikeCanvas, // #ffffff — text on black surfaces

    primaryContainer = NikeInk,
    onPrimaryContainer = NikeCanvas,

    secondary = NikeSoftCloud, // #f5f5f5 — secondary CTA bg
    onSecondary = NikeInk,

    secondaryContainer = NikeSoftCloud,
    onSecondaryContainer = NikeInk,

    tertiary = NikeAccentTeal, // editorial accent (stars, teal chips)
    onTertiary = NikeCanvas,

    tertiaryContainer = NikeAccentPurplePale,
    onTertiaryContainer = NikeInk,

    background = NikeCanvas, // #ffffff
    onBackground = NikeInk,

    surface = NikeCanvas,
    onSurface = NikeInk,

    surfaceVariant = NikeSoftCloud, // #f5f5f5 — card image stage, input bg
    onSurfaceVariant = NikeMute, // #707072 — subtitles, metadata

    outline = NikeHairline, // #cacaca — 1px dividers
    outlineVariant = NikeHairlineSoft, // #e5e5e5 — sticky bar inset

    inverseSurface = NikeInk,
    inverseOnSurface = NikeCanvas,
    inversePrimary = NikeCanvas,

    error = NikeSale, // #d30005 — the only red
    onError = NikeCanvas,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = NikeSaleDeep,

    scrim = NikeInk,
    surfaceTint = Color.Transparent,
)

// ─── Dark ─────────────────────────────────────────────────────────────────────
// Inversion logic:
//   primary/CTA      → white  (NikeDarkOnBase) so buttons stay high-contrast
//   background       → #111   (NikeDarkBase)
//   surface / cards  → #1c1c  (NikeDarkSurface) — one step lighter than bg
//   surfaceVariant   → #2e2e  (NikeDarkSurfaceTop) — input containers
//   outline          → #3a3a  (NikeDarkHairline) — visible but subtle
//   error            → lifted red (#ff4d4d) for WCAG on dark bg

private val NikeDarkColorScheme = darkColorScheme(
    primary = NikeDarkOnBase, // white — CTA on dark bg
    onPrimary = NikeDarkBase, // ink — text on white button

    primaryContainer = NikeDarkSurface,
    onPrimaryContainer = NikeDarkOnBase,

    secondary = NikeDarkSurfaceTop, // dark chip / secondary CTA bg
    onSecondary = NikeDarkOnBase,

    secondaryContainer = NikeDarkSurfaceHigh,
    onSecondaryContainer = NikeDarkOnMid,

    tertiary = NikeDarkTeal, // lifted teal for dark bg
    onTertiary = NikeDarkBase,

    tertiaryContainer = Color(0xFF1A2A3A),
    onTertiaryContainer = NikeDarkOnBase,

    background = NikeDarkBase, // #111111
    onBackground = NikeDarkOnBase, // #ffffff

    surface = NikeDarkSurface, // #1c1c1c — cards, nav
    onSurface = NikeDarkOnBase,

    surfaceVariant = NikeDarkSurfaceTop, // #2e2e2e — input bg
    onSurfaceVariant = NikeDarkOnLow, // #9e9e9e — placeholder / meta

    outline = NikeDarkHairline, // #3a3a3a — dividers
    outlineVariant = NikeDarkHairlineSoft, // #2c2c2c — inset borders

    inverseSurface = NikeCanvas,
    inverseOnSurface = NikeInk,
    inversePrimary = NikeInk,

    error = NikeDarkSale, // #ff4d4d — WCAG-safe on dark
    onError = NikeDarkBase,
    errorContainer = Color(0xFF5C0A0A),
    onErrorContainer = Color(0xFFFFB4AB),

    scrim = NikeDarkBase,
    surfaceTint = Color.Transparent,
)

// ─── AppTheme ─────────────────────────────────────────────────────────────────

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) NikeDarkColorScheme else NikeLightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
