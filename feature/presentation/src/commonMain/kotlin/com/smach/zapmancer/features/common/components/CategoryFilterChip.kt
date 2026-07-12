package com.smach.zapmancer.features.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.features.common.theme.pill

// ─── Category Filter Chip ─────────────────────────────────────────────────────
// Fully design-agnostic: all colors and shapes come from MaterialTheme tokens.
//
// Active → primary fill, onPrimary text (full inversion)
// Default → surface fill, onSurface text, outline border
// Shape  → MaterialTheme.shapes.pill (extraLarge slot)
//
// Swapping the 4 theme files (Color, Type, Shape, Theme) will fully restyle this.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChip(
    category: ProjectCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                category.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            // Active: primary bg, onPrimary text
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            // Default: surface bg, onSurface text
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            },
        ),
        // shape.pill → extraLarge slot (9999dp in Nike, easily swappable)
        shape = MaterialTheme.shapes.pill,
        modifier = modifier,
    )
}
