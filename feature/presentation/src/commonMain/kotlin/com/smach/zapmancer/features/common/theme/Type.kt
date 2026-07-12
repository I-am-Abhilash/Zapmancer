package com.smach.zapmancer.features.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Nike Design System — Typography ──────────────────────────────────────────
// Spec: DESIGN.md / Nike-design-analysis
//
// Nike proprietary fonts (Nike Futura ND, Helvetica Now) are unavailable in CMP.
// Substitution (per DESIGN.md "Note on Font Substitutes"):
//   Campaign display tier → FontFamily.SansSerif at W700 (approximates Futura ND's weight)
//   UI / body tier        → FontFamily.SansSerif at W400/W500
//
// Letter-spacing is 0 across all roles per Nike spec (Futura ND and Helvetica Now
// are optically tight; letter-spacing tightening is done at the font level).

val Typography = Typography(

    // ── Campaign display (96sp / W500 / lineHeight ~86sp = 0.9×96) ─────────
    // Used ONLY for editorial hero lockups. Never for section headers or product titles.
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp,
        lineHeight = 86.sp, // 0.9 line-height per spec
        letterSpacing = 0.sp,
    ),

    // ── display-campaign at tablet size (64sp) ───────────────────────────────
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 58.sp, // 0.9
        letterSpacing = 0.sp,
    ),

    // ── display-campaign at mobile size (48sp) ───────────────────────────────
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 43.sp, // 0.9
        letterSpacing = 0.sp,
    ),

    // ── heading-xl (32sp / W500 / lineHeight 1.2) ───────────────────────────
    // "FEATURED FOOTWEAR", "LATEST IN CLOTHING", PDP product title block
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 38.sp, // 1.2 × 32
        letterSpacing = 0.sp,
    ),

    // ── heading-lg (24sp / W500 / lineHeight 1.2) ───────────────────────────
    // Subsection titles, member-benefit card title, large CTA label, PDP price
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 29.sp, // 1.2 × 24
        letterSpacing = 0.sp,
    ),

    // ── heading-md (16sp / W500 / lineHeight 1.75) ──────────────────────────
    // Card title, FAQ row label, filter group header
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 28.sp, // 1.75 × 16
        letterSpacing = 0.sp,
    ),

    // ── body-strong (16sp / W500 / lineHeight 1.5) ──────────────────────────
    // Product card name, filter row label, primary nav link — the UI workhorse
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp, // 1.5 × 16
        letterSpacing = 0.sp,
    ),

    // ── button-md (16sp / W500 / lineHeight 1.5) ────────────────────────────
    // Standard pill CTAs across the system
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // ── button-sm (14sp / W500 / lineHeight 1.5) ────────────────────────────
    // Compact pill CTA, badge label, geo-selector button
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp, // 1.5 × 14
        letterSpacing = 0.sp,
    ),

    // ── body-md (16sp / W400 / lineHeight 1.5) ──────────────────────────────
    // Body copy, search-pill placeholder, product description
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // ── link-md (16sp / W500 / lineHeight 1.75 / underline) ─────────────────
    // Underlined inline links — the only underlined text in the system
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),

    // ── caption-md (14sp / W500 / lineHeight 1.5) ───────────────────────────
    // Product subtitle, filter count, footer link
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp,
    ),

    // ── button-lg (24sp / W500 / lineHeight 1.2) ────────────────────────────
    // Pressed-letter campaign CTA inside hero blocks
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 29.sp,
        letterSpacing = 0.sp,
    ),

    // ── caption-sm (12sp / W500 / lineHeight 1.5) ───────────────────────────
    // Filter chip label, badge text, color count
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),

    // ── utility-xs (9sp / W500 / lineHeight 1.75) ───────────────────────────
    // Legal copyright / fine-print row — the system's smallest text
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 9.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)
