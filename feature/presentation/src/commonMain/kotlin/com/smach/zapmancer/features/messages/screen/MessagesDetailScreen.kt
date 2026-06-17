package com.smach.zapmancer.features.messages.screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.common.components.AppImage
import com.smach.zapmancer.features.messages.state.MessageItem
import com.smach.zapmancer.features.messages.state.MessageStatus
import com.smach.zapmancer.features.messages.state.MessagesDetailUiState

// Flip7 Palette
private val ZapTeal = Color(0xFF2BA8A2)
private val ZapCoral = Color(0xFFEF6C4A)
private val ZapBg = Color(0xFFEFF8F7)
private val ZapSurface = Color(0xFFFFFFFF)
private val ZapOnSurface = Color(0xFF131B2E)
private val ZapOnSurfaceVariant = Color(0xFF434655)
private val ZapOutlineVariant = Color(0xFFE0E8E7)
private val ZapCream = Color(0xFFFDFBF7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    state: MessagesDetailUiState = MessagesDetailUiState(),
    onBackClick: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    onSendMessage: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppImage(
                            model = state.contactAvatarUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ZapBg)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = state.contactName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZapOnSurface
                            )
                            if (state.isOnline) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(ZapTeal)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Online",
                                        fontSize = 12.sp,
                                        color = ZapTeal,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ZapOnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Videocam, contentDescription = null, tint = ZapOnSurfaceVariant) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Call, contentDescription = null, tint = ZapOnSurfaceVariant) }
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null, tint = ZapOnSurfaceVariant) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ZapSurface),
                modifier = Modifier.border(0.5.dp, ZapOutlineVariant)
            )
        },
        bottomBar = {
            MessageInput(
                typingText = state.typingText,
                onTextChange = onTextChange,
                onSend = { onSendMessage(state.typingText) }
            )
        },
        containerColor = ZapBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = true
        ) {
            if (state.isContactTyping) {
                item { TypingIndicator() }
            }
            
            items(state.messages.reversed()) { message ->
                MessageBubble(message)
            }
            
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = ZapSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "Today, August 25",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = ZapOnSurfaceVariant,
                            fontWeight = FontWeight.Medium
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
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isFromMe) {
            AppImage(
                model = message.avatarUrl ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ZapBg)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Surface(
                color = if (message.isFromMe) ZapTeal else ZapCream,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromMe) 16.dp else 0.dp,
                    bottomEnd = if (message.isFromMe) 0.dp else 16.dp
                ),
                border = if (message.isFromMe) null else androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = if (message.isFromMe) Color.White else ZapOnSurface,
                    lineHeight = 20.sp
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = message.timestamp,
                    fontSize = 11.sp,
                    color = ZapOnSurfaceVariant
                )
                if (message.isFromMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (message.status == MessageStatus.READ) Icons.Default.DoneAll else Icons.Default.Done,
                        contentDescription = null,
                        tint = ZapTeal,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    typingText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        color = ZapSurface,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Column {
            HorizontalDivider(color = ZapOutlineVariant, thickness = 1.dp)
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ZapBg, RoundedCornerShape(12.dp))
                        .border(1.dp, ZapOutlineVariant, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    Column {
                        TextField(
                            value = typingText,
                            onValueChange = onTextChange,
                            placeholder = { Text("Write a message...", color = ZapOnSurfaceVariant) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = {}) { Icon(Icons.Outlined.AddCircle, null, tint = ZapOnSurfaceVariant) }
                                IconButton(onClick = {}) { Icon(Icons.Outlined.SentimentSatisfied, null, tint = ZapOnSurfaceVariant) }
                                IconButton(onClick = {}) { Icon(Icons.Outlined.AttachFile, null, tint = ZapOnSurfaceVariant) }
                            }
                            
                            Button(
                                onClick = onSend,
                                colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                modifier = Modifier.height(36.dp).shadow(4.dp, RoundedCornerShape(20.dp))
                            ) {
                                Text("Send", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(16.dp))
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
    val infiniteTransition = rememberInfiniteTransition()

    Row(
        modifier = Modifier.padding(start = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(3) { index ->
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = index * 200),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(ZapCoral.copy(alpha = alpha))
            )
        }
    }
}

