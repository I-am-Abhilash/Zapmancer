package com.smach.zapmancer.features.messages.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.components.shimmerEffect
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import com.smach.zapmancer.features.messages.viewmodel.MessagesListEffect
import com.smach.zapmancer.features.messages.viewmodel.MessagesListEvent
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessagesListScreen(
    viewModel: MessagesListViewModel = koinViewModel(),
    onConversationClick: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MessagesListEffect.NavigateToConversation -> onConversationClick(effect.id)
                is MessagesListEffect.NavigateToProfile -> onProfileClick(effect.id)
                is MessagesListEffect.ShowToast -> {}
            }
        }
    }

    MessagesListContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListContent(
    state: MessagesListUiState,
    onEvent: (MessagesListEvent) -> Unit,
    showTopBar: Boolean = true,
) {
    val content = @Composable { padding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            MessagesSearchAndFilter(
                searchQuery = state.searchQuery,
                selectedFilter = state.selectedFilter,
                onEvent = onEvent,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                if (state.isLoading) {
                    items(5) {
                        ConversationShimmerRow()
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp,
                        )
                    }
                } else if (state.conversations.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No conversations found",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                } else {
                    items(state.conversations, key = { it.id }) { conversation ->
                        ConversationItemRow(
                            item = conversation,
                            isSelected = false,
                            onClick = { onEvent(MessagesListEvent.ConversationClicked(conversation.id)) },
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }
    }

    if (showTopBar) {
        Scaffold(
            topBar = {
                ZapmancerTopBar(
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "Zapmancer",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    },
                    actions = {
                        UserAvatar(
                            onClick = { onEvent(MessagesListEvent.ProfileClicked("me")) },
                            imageUrl = null,
                            size = 32.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            borderWidth = 1.dp,
                            borderColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    drawBottomBorder = true,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { padding ->
            content(padding)
        }
    } else {
        content(PaddingValues(0.dp))
    }
}

@Composable
fun MessagesSearchAndFilter(
    searchQuery: String,
    selectedFilter: String,
    onEvent: (MessagesListEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { onEvent(MessagesListEvent.OnSearchQueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Search conversations...",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            ),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Unread", "Archived").forEach { filter ->
                val isSelected = filter == selectedFilter
                Surface(
                    onClick = { onEvent(MessagesListEvent.OnFilterSelected(filter)) },
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.height(32.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Text(
                            filter,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        )
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
    onClick: () -> Unit,
) {
    val drawLineColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface)
            .drawBehind {
                if (isSelected) {
                    val strokeWidth = 4.dp.toPx()
                    drawLine(
                        color = drawLineColor,
                        start = Offset(strokeWidth / 2, 0f),
                        end = Offset(strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth,
                    )
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserAvatar(
            imageUrl = item.avatarUrl,
            size = 48.dp,
            isOnline = item.isOnline,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = item.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (item.isUnread) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Composable
private fun ConversationShimmerRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .shimmerEffect(),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(16.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(12.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
            )
        }
    }
}

@Preview
@Composable
fun MessagesListScreenPreview() {
    AppTheme {
        MessagesListContent(
            state = MessagesListUiState(
                conversations = listOf(
                    ConversationItem(
                        id = "1",
                        name = "Alex Rivera",
                        avatarUrl = "",
                        lastMessage = "The deployment pipeline is successfully configured...",
                        timestamp = "09:42 AM",
                        isUnread = true,
                        isOnline = true,
                    ),
                    ConversationItem(
                        id = "2",
                        name = "Sarah Chen",
                        avatarUrl = "",
                        lastMessage = "I've reviewed the latest pull request. Just a few minor...",
                        timestamp = "Yesterday",
                        isUnread = false,
                        isOnline = false,
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}
