package com.smach.zapmancer.features.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Coinbase Design System — Typography ──────────────────────────────────────
// Spec: CoinBaseDESIGN.md / Coinbase-design-analysis
//
// Font Substitutes:
//   CoinbaseDisplay -> Inter (FontFamily.SansSerif at weight 400, negative tracking)
//   CoinbaseSans    -> Inter (FontFamily.SansSerif at weight 400/600/700)
//   CoinbaseMono    -> FontFamily.Monospace (e.g. JetBrains Mono/Geist Mono fallback)
//
// Note: Coinbase display sizes use quiet, modest weights (400, never 700+) to
// signal institutional calm and trust rather than fintech urgency.

val Typography = Typography(

    // ── display-mega (80px / W400 / lineHeight 1.0 / letterSpacing -2px) ────────
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 80.sp,
        lineHeight = 80.sp,
        letterSpacing = (-2).sp,
    ),

    // ── display-xl (64px / W400 / lineHeight 1.0 / letterSpacing -1.6px) ────────
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        lineHeight = 64.sp,
        letterSpacing = (-1.6).sp,
    ),

    // ── display-md (44px / W400 / lineHeight 1.09 / letterSpacing -1px) ─────────
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-1).sp,
    ),

    // ── display-sm (36px / W400 / lineHeight 1.11 / letterSpacing -0.5px) ───────
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
    ),

    // ── title-lg (32px / W400 / lineHeight 1.13 / letterSpacing -0.4px) ─────────
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.4).sp,
    ),

    // ── title-md (18px / W600 / lineHeight 1.33 / letterSpacing 0) ──────────────
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // ── title-sm (16px / W600 / lineHeight 1.25 / letterSpacing 0) ──────────────
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    // ── button (16px / W600 / lineHeight 1.15 / letterSpacing 0) ────────────────
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),

    // ── nav-link (14px / W500 / lineHeight 1.4 / letterSpacing 0) ───────────────
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    // ── body-md (16px / W400 / lineHeight 1.5 / letterSpacing 0) ────────────────
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // ── body-strong (16px / W700 / lineHeight 1.5 / letterSpacing 0) ────────────
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // ── body-sm (14px / W400 / lineHeight 1.5 / letterSpacing 0) ────────────────
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp,
    ),

    // ── number-display (18px / W500 / lineHeight 1.4 / letterSpacing 0 / Monospace)
    labelLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    ),

    // ── caption-strong (12px / W600 / lineHeight 1.5 / letterSpacing 0) ─────────
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),

    // ── caption (13px / W400 / lineHeight 1.5 / letterSpacing 0) ────────────────
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp,
    ),
)
