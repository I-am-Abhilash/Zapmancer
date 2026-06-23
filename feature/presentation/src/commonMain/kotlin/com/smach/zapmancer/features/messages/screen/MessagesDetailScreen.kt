package com.smach.zapmancer.features.messages.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.model.MessageStatus
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.messages.state.MessagesDetailUiState
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailEvent
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageDetailScreen(
    viewModel: MessagesDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    MessageDetailContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onCallClick = { showSnackbar("Voice calling is not supported in this beta") },
        onVideocamClick = { showSnackbar("Video calling is not supported in this beta") },
        onMoreClick = { showSnackbar("More actions are not supported in this beta") },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailContent(
    state: MessagesDetailUiState,
    onEvent: (MessagesDetailEvent) -> Unit,
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit,
    onVideocamClick: () -> Unit,
    onMoreClick: () -> Unit,
    onProfileClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            ZapmancerTopBar(
                titleContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onProfileClick(state.contactName)
                        },
                    ) {
                        UserAvatar(
                            imageUrl = state.contactAvatarUrl,
                            size = 40.dp,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                            Text(
                                text = state.contactName,
                                fontSize = 16.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            if (state.isOnline) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Online",
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            }
                        }
                    }
                },
                showBackButton = true,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { onVideocamClick() }) {
                        Icon(
                            Icons.Default.Videocam,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = { onCallClick() }) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = { onMoreClick() }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                drawBottomBorder = true,
            )
        },
        bottomBar = {
            MessageInput(
                typingText = state.typingText,
                onEvent = onEvent,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = true,
        ) {
            if (state.isContactTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        UserAvatar(
                            imageUrl = state.contactAvatarUrl,
                            size = 32.dp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 16.dp,
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shadowElevation = 1.dp,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            TypingIndicator()
                        }
                    }
                }
            }

            items(state.messages.reversed()) { message ->
                MessageBubble(message)
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            "Today, August 25",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        if (!message.isFromMe) {
            UserAvatar(
                imageUrl = message.avatarUrl,
                size = 32.dp,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp),
        ) {
            Surface(
                color = if (message.isFromMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromMe) 16.dp else 0.dp,
                    bottomEnd = if (message.isFromMe) 0.dp else 16.dp,
                ),
                border = if (message.isFromMe) {
                    null
                } else {
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                    )
                },
                shadowElevation = 1.dp,
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = if (message.isFromMe) Color.White else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
            ) {
                Text(
                    text = message.timestamp,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (message.isFromMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    val icon = when (message.status) {
                        MessageStatus.READ -> Icons.Default.DoneAll
                        else -> Icons.Default.Done
                    }
                    val tint =
                        if (message.status == MessageStatus.READ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    typingText: String,
    onEvent: (MessagesDetailEvent) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(12.dp),
                        )
                        .padding(4.dp),
                ) {
                    Column {
                        TextField(
                            value = typingText,
                            onValueChange = { onEvent(MessagesDetailEvent.OnTextChanged(it)) },
                            placeholder = {
                                Text(
                                    "Write a message...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Outlined.AddCircle,
                                        null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Outlined.SentimentSatisfied,
                                        null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Outlined.AttachFile,
                                        null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }

                            Button(
                                onClick = { onEvent(MessagesDetailEvent.SendMessage) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                modifier = Modifier.height(36.dp),
                            ) {
                                Text("Send", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    null,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "TypingIndicator")

    Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(3) { index ->
            val translationY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "DotTranslation",
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .offset(y = translationY.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Preview
@Composable
fun MessageDetailScreenPreview() {
    MaterialTheme {
        MessageDetailContent(
            state = MessagesDetailUiState(
                contactName = "Alex Rivera",
                isOnline = true,
                messages = listOf(
                    MessageItem(
                        id = "1",
                        text = "The deployment pipeline is successfully configured for the staging environment. Can you check the logs to verify everything is running smoothly?",
                        timestamp = "09:42 AM",
                        isFromMe = false,
                    ),
                    MessageItem(
                        id = "2",
                        text = "I'm on it. Just logging into the dashboard now. The CPU spikes we saw yesterday shouldn't be an issue with the new load balancer config.",
                        timestamp = "09:45 AM",
                        isFromMe = true,
                        status = MessageStatus.READ,
                    ),
                    MessageItem(
                        id = "3",
                        text = "Agreed. Let me know if you see any anomalies in the memory footprint. I'll be around for the next hour.",
                        timestamp = "09:46 AM",
                        isFromMe = false,
                    ),
                ),
                isContactTyping = true,
            ),
            onEvent = {},
            onCallClick = {},
            onVideocamClick = {},
            onMoreClick = {},
            onProfileClick = {},
        )
    }
}
