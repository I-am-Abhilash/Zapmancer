package com.smach.zapmancer.features.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.smach.zapmancer.features.common.theme.Warning

@Composable
fun ProjectStatus.color(): Color = when (this) {
    ProjectStatus.ACTIVE -> MaterialTheme.colorScheme.primary
    ProjectStatus.PENDING -> Warning
    ProjectStatus.COMPLETED -> MaterialTheme.colorScheme.outline
}

@Composable
fun ProjectStatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier,
) {
    val statusColor = status.color()
    Surface(
        color = statusColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Text(
                status.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = statusColor,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}
