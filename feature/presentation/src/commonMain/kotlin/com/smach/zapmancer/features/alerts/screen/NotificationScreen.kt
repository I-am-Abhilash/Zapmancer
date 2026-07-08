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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.NotificationAction
import com.smach.zapmancer.domain.model.NotificationItem
import com.smach.zapmancer.domain.model.NotificationType
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import com.smach.zapmancer.features.alerts.viewmodel.NotificationEffect
import com.smach.zapmancer.features.alerts.viewmodel.NotificationEvent
import com.smach.zapmancer.features.alerts.viewmodel.NotificationViewModel
import com.smach.zapmancer.features.common.adaptive.LocalWindowLayout
import com.smach.zapmancer.features.common.adaptive.WindowLayout
import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
import com.smach.zapmancer.features.common.components.LocalDrawerController
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerController = LocalDrawerController.current
    val windowLayout = rememberWindowLayout()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NotificationEffect.ShowToast -> showSnackbar(effect.message)
            }
        }
    }

    CompositionLocalProvider(LocalWindowLayout provides windowLayout) {
        Scaffold(
            topBar = {
                ZapmancerTopBar(
                    title = "Zapmancer",
                    showBackButton = windowLayout.isCompact,
                    onBackClick = onBackClick,
                    containerColor = MaterialTheme.colorScheme.surface,
                    drawBottomBorder = true,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
        ) { padding ->
            NotificationContent(
                paddingValues = padding,
                state = uiState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

@Composable
fun NotificationContent(
    paddingValues: PaddingValues,
    state: NotificationUiState,
    onEvent: (NotificationEvent) -> Unit,
) {
    val windowLayout = LocalWindowLayout.current
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = windowLayout.contentMaxWidthDp.dp)
                .padding(horizontal = windowLayout.screenHorizontalPaddingDp.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp),
        ) {
            val grouped = state.notifications.groupBy { it.section }
            grouped.forEach { (section, items) ->
                item { RibbonHeader(section) }
                items(items) { item ->
                    NotificationCard(
                        item = item,
                        replyText = state.replyDrafts[item.id] ?: "",
                        onReplyTextChanged = { text -> onEvent(NotificationEvent.OnReplyTextChanged(item.id, text)) },
                        onSendReply = { onEvent(NotificationEvent.SendQuickReply(item.id)) },
                        onActionClicked = { actionLabel -> onEvent(NotificationEvent.ExecuteAction(item.id, actionLabel)) },
                    )
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
            modifier = Modifier.padding(start = 4.dp).offset(x = (-20).dp),
            shadowElevation = 4.dp,
        ) {
            Text(
                text = text.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 1.1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
        val pathColor = MaterialTheme.colorScheme.primaryContainer
        Canvas(
            modifier = Modifier.size(8.dp).align(Alignment.BottomStart).offset(x = (-16).dp, y = 8.dp),
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
fun NotificationCard(
    item: NotificationItem,
    replyText: String,
    onReplyTextChanged: (String) -> Unit,
    onSendReply: () -> Unit,
    onActionClicked: (String) -> Unit,
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val icon = when (item.type) {
        NotificationType.MILESTONE -> Icons.Outlined.Work
        NotificationType.MESSAGE -> Icons.Outlined.ChatBubble
        NotificationType.ALERT -> Icons.Outlined.Warning
        NotificationType.GENERAL -> Icons.Outlined.Sync
        NotificationType.COLLABORATOR -> Icons.Outlined.PersonAdd
    }
    val iconBg = when (item.type) {
        NotificationType.MILESTONE, NotificationType.MESSAGE -> MaterialTheme.colorScheme.primaryContainer
        NotificationType.ALERT -> MaterialTheme.colorScheme.errorContainer
        NotificationType.GENERAL, NotificationType.COLLABORATOR -> MaterialTheme.colorScheme.surfaceVariant
    }
    val iconTint = when (item.type) {
        NotificationType.MILESTONE, NotificationType.MESSAGE -> MaterialTheme.colorScheme.primary
        NotificationType.ALERT -> MaterialTheme.colorScheme.error
        NotificationType.GENERAL, NotificationType.COLLABORATOR -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val opacity = if (item.section == "Yesterday") 0.8f else 1f

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(if (item.section == "Today") 2.dp else 0.dp, MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = opacity)),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().drawAccentLine(accentColor).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.small).background(iconBg)
                    .border(1.dp, iconBg.copy(alpha = 0.1f), MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (item.type == NotificationType.ALERT) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(item.timestamp, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                if (item.isItalic) {
                    Text("\"${item.description}\"", style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic), color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
                } else {
                    Text(text = item.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
                }
                if (item.actions.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.actions.forEach { action ->
                            Button(
                                onClick = { onActionClicked(action.label) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (action.isPrimary) (if (action.isError) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary) else Color.Transparent,
                                    contentColor = if (action.isPrimary) (if (action.isError) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary) else MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                shape = MaterialTheme.shapes.small,
                                border = if (!action.isPrimary) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp),
                            ) {
                                Text(action.label, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
                if (item.quickReply) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = onReplyTextChanged,
                            placeholder = { Text("Quick reply...", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.small,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            ),
                        )
                        IconButton(
                            onClick = onSendReply,
                            modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
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
            size = Size(width = 6.dp.toPx(), height = size.height),
        )
    },
)

@PreviewScreenSizes
@Preview
@Composable
fun NotificationPreview() {
    val sampleState = NotificationUiState(
        notifications = listOf(
            NotificationItem(id = "1", type = NotificationType.MILESTONE, title = "Neural Mesh Deployment", description = "Automated deployment of the v2.4.1-alpha build was successful on the production cluster.", timestamp = "2m ago", section = "Today", actions = listOf(NotificationAction("View Logs", isPrimary = true), NotificationAction("Dismiss"))),
            NotificationItem(id = "2", type = NotificationType.MESSAGE, title = "Message from Sarah Connor", description = "The latency on the North-East edge node has stabilized. Should we increase the load distribution?", timestamp = "1h ago", section = "Today", isItalic = true, quickReply = true),
            NotificationItem(id = "3", type = NotificationType.ALERT, title = "Database Connection Spike", description = "Unauthorized access attempts detected from IP 192.168.1.104. Security protocols initiated.", timestamp = "4h ago", section = "Today", actions = listOf(NotificationAction("Block IP", isPrimary = true, isError = true), NotificationAction("Investigate"))),
            NotificationItem(id = "4", type = NotificationType.GENERAL, title = "Weekly Backup Complete", description = "All system partitions have been mirrored to the secure vault. Integrity check: 100%.", timestamp = "1d ago", section = "Yesterday"),
            NotificationItem(id = "5", type = NotificationType.COLLABORATOR, title = "New Collaborator Joined", description = "David Chen was added to the \"Project Phoenix\" team by Admin.", timestamp = "1d ago", section = "Yesterday"),
        ),
    )
    CompositionLocalProvider(LocalWindowLayout provides WindowLayout.Compact) {
        NotificationContent(
            paddingValues = PaddingValues(0.dp),
            state = sampleState,
            onEvent = {},
        )
    }
}
