package com.smach.zapmancer.features.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smach.zapmancer.domain.model.ProjectCategory

@Composable
fun categoryAccentColor(category: ProjectCategory): Color {
    return when (category) {
        ProjectCategory.ALL -> MaterialTheme.colorScheme.primary
        ProjectCategory.DESIGN -> MaterialTheme.colorScheme.tertiary
        ProjectCategory.DEVELOPMENT -> MaterialTheme.colorScheme.primary
        ProjectCategory.MARKETING -> MaterialTheme.colorScheme.secondary
        ProjectCategory.WRITING -> MaterialTheme.colorScheme.secondary
        ProjectCategory.MULTIMEDIA -> MaterialTheme.colorScheme.tertiary
        ProjectCategory.CONSULTING -> MaterialTheme.colorScheme.primary
        ProjectCategory.ADMIN -> MaterialTheme.colorScheme.outline
        ProjectCategory.FINANCE -> MaterialTheme.colorScheme.primary
        ProjectCategory.LEGAL -> MaterialTheme.colorScheme.tertiary
        ProjectCategory.ANALYTICS -> MaterialTheme.colorScheme.secondary
        ProjectCategory.SECURITY -> MaterialTheme.colorScheme.primary
        ProjectCategory.CUSTOMER_SUPPORT -> MaterialTheme.colorScheme.outline
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChip(
    category: ProjectCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tintColor = categoryAccentColor(category)
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                category.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = tintColor.copy(alpha = 0.15f),
            selectedLabelColor = tintColor,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) tintColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
    )
}
