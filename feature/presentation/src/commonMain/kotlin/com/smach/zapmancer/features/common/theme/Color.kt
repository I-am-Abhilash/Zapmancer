package com.smach.zapmancer.features.common.theme

import androidx.compose.ui.graphics.Color

// ─── Nike Design System — Color Tokens ────────────────────────────────────────
// Source: DESIGN.md  /  Nike-design-analysis

// Brand core
val NikeInk = Color(0xFF111111) // primary — CTA, headline, active chip
val NikeCanvas = Color(0xFFFFFFFF) // on-primary / page background
val NikeSoftCloud = Color(0xFFF5F5F5) // soft surface — card stage, search pill, secondary CTA

// Text scale
val NikeCharcoal = Color(0xFF39393B) // body where ink is too heavy
val NikeAsh = Color(0xFF4B4B4D) // disabled / low-emphasis utility
val NikeMute = Color(0xFF707072) // subtitles, footer links, secondary meta
val NikeStone = Color(0xFF9E9EA0) // inverse secondary text / lowest emphasis

// Divider / hairline (the only "elevation" in the system)
val NikeHairline = Color(0xFFCACACA) // 1px dividers — filter rows, footer, PDP rows
val NikeHairlineSoft = Color(0xFFE5E5E5) // inset shadow on sticky bars

// Semantic
val NikeSale = Color(0xFFD30005) // discounted price / error — the only red
val NikeSaleDeep = Color(0xFF780700) // sale hover / dark-mode sale anchor
val NikeSuccess = Color(0xFF007D48) // confirmations, in-stock, eligibility ticks
val NikeSuccessBright = Color(0xFF1EAA52) // inverse success on dark surfaces
val NikeInfo = Color(0xFF1151FF) // informational link / member callouts
val NikeInfoDeep = Color(0xFF0034E3) // pressed info accent

// Category accents — used ONLY for swatch dots / soft tile fills / editorial chips
val NikeAccentPink = Color(0xFFED1AA0)
val NikeAccentPinkSoft = Color(0xFFFFB0DD)
val NikeAccentPurpleSoft = Color(0xFFBEAFFD)
val NikeAccentPurplePale = Color(0xFFD6D1FF)
val NikeAccentTeal = Color(0xFF0A7281)
val NikeAccentPinkDeep = Color(0xFF4C012D)

// ─── Nike Dark Mode Palette ───────────────────────────────────────────────────
// Dark mode is a direct inversion of the light system:
//   Canvas (#fff) becomes the primary/CTA (white buttons on dark bg)
//   Ink (#111) becomes the background surface
// Warm graphite steps are used instead of pure black to reduce harshness.

// Dark surfaces (background → container, deepest → lightest)
val NikeDarkBase = Color(0xFF111111) // deepest bg — same as NikeInk
val NikeDarkSurface = Color(0xFF1C1C1C) // cards, nav — one step up from base
val NikeDarkSurfaceHigh = Color(0xFF242424) // elevated cards, modals
val NikeDarkSurfaceTop = Color(0xFF2E2E2E) // input container, chip bg

// Dark text scale (on dark surfaces)
val NikeDarkOnBase = Color(0xFFFFFFFF) // primary text — pure white
val NikeDarkOnMid = Color(0xFFD4D4D4) // secondary text
val NikeDarkOnLow = Color(0xFF9E9E9E) // tertiary / placeholder
val NikeDarkOnMute = Color(0xFF6B6B6B) // disabled / decorative

// Dark dividers
val NikeDarkHairline = Color(0xFF3A3A3A) // 1px row dividers
val NikeDarkHairlineSoft = Color(0xFF2C2C2C) // inset border on inputs

// Dark semantic (same hue, adjusted lightness for WCAG contrast on dark bg)
val NikeDarkSale = Color(0xFFFF4D4D) // sale / error on dark — brighter red
val NikeDarkSuccess = Color(0xFF1EAA52) // NikeSuccessBright — readable on dark
val NikeDarkInfo = Color(0xFF4D7CFF) // info blue lifted for dark bg contrast
val NikeDarkTeal = Color(0xFF1AAEC0) // accent teal lifted for dark bg
