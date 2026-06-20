package com.smach.zapmancer.features.alerts.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.alerts.state.NotificationAction
import com.smach.zapmancer.features.alerts.state.NotificationItem
import com.smach.zapmancer.features.alerts.state.NotificationType
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import com.smach.zapmancer.features.common.theme.ZapCoral
import com.smach.zapmancer.features.common.theme.ZapGold
import com.smach.zapmancer.features.common.theme.ZapSlate
import com.smach.zapmancer.features.common.theme.ZapTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    state: NotificationUiState,
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Zapmancer",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
        ) {
           val grouped = state.notifications.groupBy { it.section }

            grouped.forEach { (section, items) ->
                item {
                    RibbonHeader(section)
                }
                items(items) { item ->
                    NotificationCard(item)
                }
            }
        }
    }
}

@Composable
fun RibbonHeader(text: String) {
    Box(modifier = Modifier.padding(start = 4.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 4.dp) // Adjust for the "fold"
                .offset(x = (-20).dp),
            shadowElevation = 4.dp
        ) {
            Text(
                text = text.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 1.1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        val pathColor = MaterialTheme.colorScheme.primaryContainer
        Canvas(
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-16).dp, y = 8.dp)
        ) {
            val path = Path().apply {
                moveTo(16f, 0f)
                lineTo(16f, 16f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(path, color = pathColor)
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem) {
    val accentColor = when (item.type) {
        NotificationType.MILESTONE -> ZapGold
        NotificationType.MESSAGE -> ZapTeal
        NotificationType.ALERT -> ZapCoral
        NotificationType.GENERAL, NotificationType.COLLABORATOR -> ZapSlate
    }

    val icon = when (item.type) {
        NotificationType.MILESTONE -> Icons.Outlined.Work
        NotificationType.MESSAGE -> Icons.Outlined.ChatBubble
        NotificationType.ALERT -> Icons.Outlined.Warning
        NotificationType.GENERAL -> Icons.Outlined.Sync
        NotificationType.COLLABORATOR -> Icons.Outlined.PersonAdd
    }

    val iconBg = when (item.type) {
        NotificationType.MILESTONE -> Color(0xFFFEF9C3) // yellow-50
        NotificationType.MESSAGE -> ZapTeal.copy(alpha = 0.05f)
        NotificationType.ALERT -> Color(0xFFFEF2F2) // red-50
        NotificationType.GENERAL, NotificationType.COLLABORATOR -> Color(0xFFF9FAFB) // gray-50
    }

    val iconTint = when (item.type) {
        NotificationType.MILESTONE -> Color(0xFFCA8A04) // yellow-600
        NotificationType.MESSAGE -> ZapTeal
        NotificationType.ALERT -> ZapCoral
        NotificationType.GENERAL, NotificationType.COLLABORATOR -> Color(0xFF6B7280) // gray-500
    }
    val opacity = if (item.section == "Yesterday") 0.8f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (item.section == "Today") 2.dp else 0.dp, RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = opacity)),
        shape = RoundedCornerShape(
            topStart = 2.dp,
            bottomStart = 2.dp,
            topEnd = 8.dp,
            bottomEnd = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawAccentLine(accentColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBg)
                    .border(1.dp, iconBg.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = if (item.type == NotificationType.ALERT) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        item.timestamp,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }

                Spacer(Modifier.height(4.dp))

                if (item.isItalic) {
                    Text(
                        "\"${item.description}\"",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                } else {
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                }

                if (item.actions.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.actions.forEach { action ->
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (action.isPrimary) (if (action.isError) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary) else Color.Transparent,
                                    contentColor = if (action.isPrimary) Color.White else MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.7f
                                    )
                                ),
                                shape = RoundedCornerShape(4.dp),
                                border = if (!action.isPrimary) BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline
                                ) else null,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(action.label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (item.quickReply) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Quick reply...", fontSize = 14.sp) },
                            modifier = Modifier
                                .weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background
                            )
                        )
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.drawAccentLine(color: Color) = this.then(
    Modifier.drawBehind {
        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(width = 6.dp.toPx(), height = size.height)
        )
    }
)

@PreviewLightDark
@Composable
fun NotificationPreview(){
    val sampleState = NotificationUiState(
        notifications = listOf(
            NotificationItem(
                id = "1",
                type = NotificationType.MILESTONE,
                title = "Neural Mesh Deployment",
                description = "Automated deployment of the v2.4.1-alpha build was successful on the production cluster.",
                timestamp = "2m ago",
                section = "Today",
                actions = listOf(
                    NotificationAction("View Logs", isPrimary = true),
                    NotificationAction("Dismiss")
                )
            ),
            NotificationItem(
                id = "2",
                type = NotificationType.MESSAGE,
                title = "Message from Sarah Connor",
                description = "The latency on the North-East edge node has stabilized. Should we increase the load distribution?",
                timestamp = "1h ago",
                section = "Today",
                isItalic = true,
                quickReply = true
            ),
            NotificationItem(
                id = "3",
                type = NotificationType.ALERT,
                title = "Database Connection Spike",
                description = "Unauthorized access attempts detected from IP 192.168.1.104. Security protocols initiated.",
                timestamp = "4h ago",
                section = "Today",
                actions = listOf(
                    NotificationAction("Block IP", isPrimary = true, isError = true),
                    NotificationAction("Investigate")
                )
            ),
            NotificationItem(
                id = "4",
                type = NotificationType.GENERAL,
                title = "Weekly Backup Complete",
                description = "All system partitions have been mirrored to the secure vault. Integrity check: 100%.",
                timestamp = "1d ago",
                section = "Yesterday"
            ),
            NotificationItem(
                id = "5",
                type = NotificationType.COLLABORATOR,
                title = "New Collaborator Joined",
                description = "David Chen was added to the \"Project Phoenix\" team by Admin.",
                timestamp = "1d ago",
                section = "Yesterday"
            )
        )
    )
    NotificationScreen(state = sampleState)
}
