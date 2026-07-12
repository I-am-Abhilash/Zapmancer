package com.smach.zapmancer.features.common.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

// ─── Nike Design System — Shape Scale ─────────────────────────────────────────
// Source: DESIGN.md  →  "Border Radius Scale"
//
// Material3 slot mapping:
//   extraSmall  → rounded.none  (0dp)    — cards, image tiles, containers (flat catalog)
//   small       → rounded.sm   (18dp)    — avatar containers, small icon wells
//   medium      → rounded.md   (24dp)    — search pill, text field input
//   large       → rounded.lg   (30dp)    — all CTA pill buttons / filter chips
//   extraLarge  → rounded.full (9999dp)  — icon-circular buttons, swatch dots
//
// ─── Semantic Shape Extensions ────────────────────────────────────────────────
// Use these throughout the codebase instead of raw RoundedCornerShape(N.dp).
// Changing the shape scale above will cascade everywhere automatically.
//
//   MaterialTheme.shapes.none   → flat container / card tile
//   MaterialTheme.shapes.pill   → CTA button / filter chip (= extraLarge = 9999dp)
//   MaterialTheme.shapes.input  → text field / search pill (= medium = 24dp)
//   MaterialTheme.shapes.avatar → avatar/icon well (= small = 18dp)
//   MaterialTheme.shapes.card   → card container (= extraSmall = 0dp in Nike)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(30.dp),
    extraLarge = RoundedCornerShape(9999.dp),
)

// ─── Semantic aliases (read-only composable properties) ───────────────────────
// These let screens write MaterialTheme.shapes.pill instead of
// RoundedCornerShape(9999.dp), keeping shape decisions in Shape.kt only.

/** Pill — all CTA buttons, filter chips. Maps to shapes.extraLarge. */
val androidx.compose.material3.Shapes.pill
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraLarge

/** Input — text fields, search bars. Maps to shapes.medium. */
val androidx.compose.material3.Shapes.input
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.medium

/** Avatar — avatar containers, icon wells. Maps to shapes.small. */
val androidx.compose.material3.Shapes.avatar
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.small

/** Card — flat content tiles. Maps to shapes.extraSmall. */
val androidx.compose.material3.Shapes.card
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.extraSmall

/** Button — standard CTA pill. Maps to shapes.large. */
val androidx.compose.material3.Shapes.button
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.shapes.large
