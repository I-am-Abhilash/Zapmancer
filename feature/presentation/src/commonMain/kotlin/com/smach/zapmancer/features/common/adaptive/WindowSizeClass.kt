package com.smach.zapmancer.features.common.adaptive

import androidx.window.core.layout.WindowSizeClass

// ==========================================
// WIDTH HELPERS (Controls Columns & Sidebars)
// ==========================================

/**
 * COMPACT WIDTH (< 600dp)
 * Standard mobile phones in portrait mode.
 */
val WindowSizeClass.isCompactWidth: Boolean
    get() = !this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

/**
 * MEDIUM WIDTH (600dp - 839dp)
 * Tablets in portrait mode, or unfolded folding phones.
 */
val WindowSizeClass.isMediumWidth: Boolean
    get() = this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) &&
            !this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

/**
 * EXPANDED WIDTH (840dp+)
 * Tablets in landscape, desktop monitors, and web browsers.
 */
val WindowSizeClass.isExpandedWidth: Boolean
    get() = this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

/**
 * CUSTOM HELPER: isWideScreen
 * True if the screen is Medium OR Expanded (600dp+).
 */
val WindowSizeClass.isWideScreen: Boolean
    get() = this.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)


// ==========================================
// HEIGHT HELPERS (Controls Scrolling & Visibility)
// ==========================================

/**
 * COMPACT HEIGHT (< 480dp)
 * Phone held horizontally (Landscape mode).
 */
val WindowSizeClass.isCompactHeight: Boolean
    get() = !this.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

/**
 * MEDIUM HEIGHT (480dp - 899dp)
 * Standard phone portrait height, or tablet landscape.
 */
val WindowSizeClass.isMediumHeight: Boolean
    get() = this.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND) &&
            !this.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)

/**
 * EXPANDED HEIGHT (900dp+)
 * Extremely tall screens (large tablets in portrait, vertical monitors).
 */
val WindowSizeClass.isExpandedHeight: Boolean
    get() = this.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)


// ==========================================
// DEVICE ORIENTATION HELPERS
// ==========================================

/**
 * True if the user is holding a standard phone sideways.
 * (Width is Wide, but height is constrained).
 */
val WindowSizeClass.isLandscapeMobile: Boolean
    get() = this.isWideScreen && this.isCompactHeight