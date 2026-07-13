package com.smach.zapmancer.features.common.theme

import androidx.compose.ui.graphics.Color

// ─── Coinbase Design System — Color Tokens ─────────────────────────────────────
// Source: CoinBaseDESIGN.md / Coinbase-design-analysis
//
// Philosophy:
//   95%+ of the UI is white canvas + ink + soft gray elevation.
//   Coinbase Blue (#0052ff) is the SINGLE brand voltage — used scarcely on
//   primary CTAs, wordmark, and inline accent links. Never as a background fill.
//   Trading green/red are SEMANTIC ONLY — text color, never background.

// ─── Brand ────────────────────────────────────────────────────────────────────
val CoinbaseBlue = Color(0xFF0052FF) // primary — every CTA pill, wordmark, brand links
val CoinbaseBlueActive = Color(0xFF003ECC) // press/active darkened state
val CoinbaseBlueDisabled = Color(0xFFA8B8CC) // disabled CTA — faded blue tint

// ─── Accent ───────────────────────────────────────────────────────────────────
val CoinbaseYellow = Color(0xFFF4B000) // Bitcoin glyph / illustrative-only — not an action color

// ─── Surfaces ─────────────────────────────────────────────────────────────────
val CoinbaseCanvas = Color(0xFFFFFFFF) // default page floor
val CoinbaseSurfaceSoft = Color(0xFFF7F7F7) // subtle alternating band surface
val CoinbaseSurfaceStrong = Color(0xFFEEF0F3) // secondary button bg, search pill, asset icon plate
val CoinbaseSurfaceDark = Color(0xFF0A0B0D) // full-bleed dark hero / CTA band
val CoinbaseSurfaceElevated = Color(0xFF16181C) // floating product-UI cards inside dark heroes

// ─── Text ─────────────────────────────────────────────────────────────────────
val CoinbaseInk = Color(0xFF0A0B0D) // display headings, primary nav, body emphasis
val CoinbaseBody = Color(0xFF5B616E) // default running text — cool gray
val CoinbaseMuted = Color(0xFF7C828A) // sub-titles, breadcrumbs, footer secondary
val CoinbaseMutedSoft = Color(0xFFA8ACB3) // disabled link text / on-dark secondary text
val CoinbaseOnPrimary = Color(0xFFFFFFFF) // white text on Coinbase Blue CTAs
val CoinbaseOnDark = Color(0xFFFFFFFF) // white text on dark heroes

// ─── Hairlines ────────────────────────────────────────────────────────────────
val CoinbaseHairline = Color(0xFFDEE1E6) // default 1px divider on white surfaces
val CoinbaseHairlineSoft = Color(0xFFEEF0F3) // lighter — same hex as SurfaceStrong

// ─── Trading Semantics (text color ONLY — never background fill) ───────────────
val CoinbaseUp = Color(0xFF05B169) // price up / success / in-stock green
val CoinbaseDown = Color(0xFFCF202F) // price down / error / out-of-stock red

// ─── Dark Mode Surface Scale ───────────────────────────────────────────────────
// Dark mode uses the dark hero palette as the full-app surface.
// Coinbase Blue remains the primary CTA on dark surfaces (no inversion).
val CoinbaseDarkBase = Color(0xFF0A0B0D) // deepest bg — SurfaceDark
val CoinbaseDarkSurface = Color(0xFF16181C) // cards — SurfaceElevated
val CoinbaseDarkSurfaceMid = Color(0xFF1E2026) // modals, raised panels
val CoinbaseDarkSurfaceTop = Color(0xFF272B33) // input containers, chip bg
val CoinbaseDarkHairline = Color(0xFF2C2F38) // 1px dividers on dark
val CoinbaseDarkOnSurface = Color(0xFFFFFFFF) // primary text
val CoinbaseDarkOnMuted = Color(0xFFA8ACB3) // secondary text
val CoinbaseDarkOnLow = Color(0xFF7C828A) // placeholder / tertiary
val CoinbaseDarkUp = Color(0xFF00C97B) // lifted green for dark bg WCAG
val CoinbaseDarkDown = Color(0xFFFF4D58) // lifted red for dark bg WCAG
