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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.features.common.adaptive.AdaptiveScaffold
import com.smach.zapmancer.features.common.adaptive.LocalWindowLayout
import com.smach.zapmancer.features.common.adaptive.NavDestination
import com.smach.zapmancer.features.common.adaptive.TwoPane
import com.smach.zapmancer.features.common.adaptive.WindowLayout
import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
import com.smach.zapmancer.features.common.components.LocalDrawerController
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import com.smach.zapmancer.features.messages.viewmodel.MessagesListEvent
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessagesListScreen(
    viewModel: MessagesListViewModel = koinViewModel(),
    onConversationClick: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    currentRoute: String = NavDestination.Messages.route,
) {
    val state by viewModel.uiState.collectAsState()
    val drawerController = LocalDrawerController.current
    val windowLayout = rememberWindowLayout()

    CompositionLocalProvider(LocalWindowLayout provides windowLayout) {
        AdaptiveScaffold(
            currentRoute = currentRoute,
            onNavigate = { dest ->
                when (dest) {
                    NavDestination.Home -> onNavigateToHome()
                    NavDestination.Projects -> onNavigateToProjects()
                    NavDestination.Messages -> Unit
                    NavDestination.Notifications -> onNavigateToNotifications()
                    NavDestination.Profile -> onProfileClick("me")
                }
            },
            title = {
                ZapmancerTopBar(
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "Zapmancer",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                            )
                        }
                    },
                    showMenuButton = windowLayout.isCompact,
                    onMenuClick = { drawerController.open() },
                    actions = {
                        UserAvatar(
                            onClick = { onProfileClick("me") },
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
        ) { padding ->
            MessagesListBody(
                paddingValues = padding,
                state = state,
                onEvent = viewModel::onEvent,
                onConversationClick = onConversationClick,
            )
        }
    }
}

@Composable
fun MessagesListBody(
    paddingValues: PaddingValues,
    state: MessagesListUiState,
    onEvent: (MessagesListEvent) -> Unit,
    onConversationClick: (String) -> Unit,
) {
    val windowLayout = LocalWindowLayout.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter,
    ) {
        TwoPane(
            windowLayout = windowLayout,
            primary = {
                MessagesListPane(
                    state = state,
                    onEvent = onEvent,
                    onConversationClick = onConversationClick,
                )
            },
            secondary = {
                if (!windowLayout.isCompact) {
                    MessagesDetailPlaceholder()
                }
            },
        )
    }
}

@Composable
fun MessagesListPane(
    state: MessagesListUiState,
    onEvent: (MessagesListEvent) -> Unit,
    onConversationClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
            if (state.conversations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize().padding(32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No conversations found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
            items(state.conversations, key = { it.id }) { conversation ->
                ConversationItemRow(
                    item = conversation,
                    isSelected = false,
                    onClick = { onConversationClick(conversation.id) },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
private fun MessagesDetailPlaceholder() {
    val windowLayout = LocalWindowLayout.current
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Select a conversation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "Pick a conversation on the left to view messages.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp).widthIn(max = 320.dp),
            )
        }
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
            placeholder = { Text("Search conversations...", fontSize = 14.sp) },
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
                    contentColor = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(width = 0.dp, height = 32.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.timestamp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = item.lastMessage,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (item.isUnread) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(10.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Preview
@Composable
fun MessagesListScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalWindowLayout provides WindowLayout.Compact) {
            MessagesListBody(
                paddingValues = PaddingValues(0.dp),
                state = MessagesListUiState(
                    conversations = listOf(
                        ConversationItem(id = "1", name = "Alex Rivera", avatarUrl = "", lastMessage = "The deployment pipeline is successfully configured...", timestamp = "09:42 AM", isUnread = true, isOnline = true),
                        ConversationItem(id = "2", name = "Sarah Chen", avatarUrl = "", lastMessage = "I've reviewed the latest pull request. Just a few minor...", timestamp = "Yesterday", isUnread = false, isOnline = false),
                    ),
                ),
                onEvent = {},
                onConversationClick = {},
            )
        }
    }
}