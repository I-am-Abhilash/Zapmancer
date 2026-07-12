package com.smach.zapmancer.features.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.features.common.theme.pill

// ─── Project Status Badge ─────────────────────────────────────────────────────
// Fully design-agnostic: status semantic colors come from MaterialTheme slots.
//
//   ACTIVE    → colorScheme.tertiary       (green / success-like tertiary)
//   PENDING   → colorScheme.secondary      (info-like secondary)
//   COMPLETED → colorScheme.onSurfaceVariant (muted / low-emphasis)
//
// In the Nike theme:
//   tertiary      = NikeInfo  (#1151ff)     ← repurposed as info
//   secondary     = NikeSoftCloud           ← NOT semantic enough for pending
//
// To make status semantics fully portable, add them to Color.kt as Material3
// custom color roles via CompositionLocal or use the error/tertiary/secondary
// slots which are the closest Material3 analogues.
//
// Badge shape: shapes.pill (extraLarge slot — changes with Shape.kt)

@Composable
fun ProjectStatus.badgeColor(): Color = when (this) {
    ProjectStatus.ACTIVE -> MaterialTheme.colorScheme.tertiary
    ProjectStatus.PENDING -> MaterialTheme.colorScheme.secondary
    ProjectStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
}

@Composable
fun ProjectStatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier,
) {
    val color = status.badgeColor()
    Surface(
        color = color.copy(alpha = 0.10f),
        shape = MaterialTheme.shapes.pill,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color),
            )
            Text(
                status.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
