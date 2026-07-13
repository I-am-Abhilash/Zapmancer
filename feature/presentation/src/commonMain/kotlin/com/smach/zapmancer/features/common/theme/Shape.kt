package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

// ─── Coinbase Design System — Shape Scale ─────────────────────────────────────
// Source: CoinBaseDESIGN.md  →  "Border Radius Scale"
//
// Material3 slot mapping:
//   extraSmall  → rounded.xs   (4dp)    — inline tags
//   small       → rounded.sm   (8dp)    — compact rows, input elements
//   medium      → rounded.md   (12dp)   — form inputs (text-input)
//   large       → rounded.lg   (16dp)   — mid-size cards
//   extraLarge  → rounded.pill (100dp)  — all CTA buttons, search pills, badges
//
// Note: rounded.xl (24dp) can be mapped to large or extraLarge if needed.
//
// ─── Semantic Shape Extensions ────────────────────────────────────────────────
// Use these throughout the codebase instead of raw RoundedCornerShape(N.dp).
// Changing the shape scale above will cascade everywhere automatically.
//
//   MaterialTheme.shapes.pill   → CTA button / filter chip (= extraLarge = 100dp)
//   MaterialTheme.shapes.input  → text field (= medium = 12dp)
//   MaterialTheme.shapes.avatar → avatar/icon well (= extraLarge/9999dp)
//   MaterialTheme.shapes.card   → card container (= large = 16dp)
//   MaterialTheme.shapes.button → CTA button (= extraLarge = 100dp)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(100.dp),
)

// ─── Semantic aliases (read-only composable properties) ───────────────────────

/** Pill — all CTA buttons, filter chips. Maps to shapes.extraLarge. */
val androidx.compose.material3.Shapes.pill
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraLarge

/** Input — text fields, search bars. Maps to shapes.medium. */
val androidx.compose.material3.Shapes.input
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.medium

/** Avatar — avatar containers, icon wells. Maps to shapes.extraLarge. */
val androidx.compose.material3.Shapes.avatar
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraLarge

/** Card — card content tiles. Maps to shapes.large. */
val androidx.compose.material3.Shapes.card
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.large

/** Button — standard CTA pill. Maps to shapes.extraLarge. */
val androidx.compose.material3.Shapes.button
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraLarge
