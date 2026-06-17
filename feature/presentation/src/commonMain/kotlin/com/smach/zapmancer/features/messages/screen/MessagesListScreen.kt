package com.smach.zapmancer.features.messages.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.common.components.AppImage
import com.smach.zapmancer.features.messages.state.ConversationItem
import com.smach.zapmancer.features.messages.state.MessagesListUiState

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
fun MessagesListScreen(
    state: MessagesListUiState = MessagesListUiState(),
    onConversationClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = ZapTeal)
                        Text(
                            "Zapmancer",
                            color = ZapTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ZapOnSurface)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ZapOutlineVariant)
                            .border(1.dp, ZapOutlineVariant, CircleShape)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ZapSurface),
                modifier = Modifier.border(0.5.dp, ZapOutlineVariant)
            )
        },
        containerColor = ZapBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MessagesSearchAndFilter(
                searchQuery = state.searchQuery,
                selectedFilter = state.selectedFilter
            )
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ZapSurface)
            ) {
                items(state.conversations) { conversation ->
                    ConversationItemRow(
                        item = conversation,
                        isSelected = false, // In mobile view, selection might not be visible as it navigates
                        onClick = { onConversationClick(conversation.id) }
                    )
                    HorizontalDivider(color = ZapOutlineVariant.copy(alpha = 0.5f), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun MessagesSearchAndFilter(
    searchQuery: String,
    selectedFilter: String
) {
    Column(
        modifier = Modifier
            .background(ZapSurface)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = { Text("Search conversations...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ZapCream,
                unfocusedContainerColor = ZapCream,
                focusedBorderColor = ZapTeal,
                unfocusedBorderColor = ZapTeal.copy(alpha = 0.5f)
            )
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Unread", "Archived").forEach { filter ->
                val isSelected = filter == selectedFilter
                Surface(
                    onClick = {},
                    color = if (isSelected) ZapTeal else ZapBg,
                    contentColor = if (isSelected) Color.White else ZapOnSurfaceVariant,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(filter, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItemRow(
    item: ConversationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) ZapBg else ZapSurface)
            .drawBehind {
                if (isSelected) {
                    val strokeWidth = 4.dp.toPx()
                    drawLine(
                        color = ZapTeal,
                        start = Offset(strokeWidth / 2, 0f),
                        end = Offset(strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AppImage(
                model = item.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ZapBg)
            )
            if (item.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(ZapTeal)
                        .border(2.dp, if (isSelected) ZapBg else ZapSurface, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ZapOnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.timestamp,
                    fontSize = 12.sp,
                    color = ZapOnSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = item.lastMessage,
                fontSize = 14.sp,
                color = ZapOnSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        if (item.isUnread) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(ZapCoral)
            )
        }
    }
}

@Preview
@Composable
fun MessagesListScreenPreview() {
    MaterialTheme {
        MessagesListScreen(
            state = MessagesListUiState(
                conversations = listOf(
                    ConversationItem(
                        id = "1",
                        name = "Alex Rivera",
                        avatarUrl = "",
                        lastMessage = "The deployment pipeline is successfully configured...",
                        timestamp = "09:42 AM",
                        isUnread = true,
                        isOnline = true
                    ),
                    ConversationItem(
                        id = "2",
                        name = "Sarah Chen",
                        avatarUrl = "",
                        lastMessage = "I've reviewed the latest pull request. Just a few minor...",
                        timestamp = "Yesterday",
                        isUnread = false,
                        isOnline = false
                    )
                )
            )
        )
    }
}
