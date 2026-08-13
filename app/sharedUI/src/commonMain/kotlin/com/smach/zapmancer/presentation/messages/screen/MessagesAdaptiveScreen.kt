package com.smach.zapmancer.presentation.messages.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smach.zapmancer.presentation.common.adaptive.isWideScreen
import com.smach.zapmancer.presentation.common.components.EmptyState
import com.smach.zapmancer.presentation.common.components.VerticalDivider

@Composable
fun MessagesAdaptiveScreen(
    onConversationClick: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var selectedConversationId by remember { mutableStateOf<String?>(null) }

    if (!windowSizeClass.isWideScreen) {
        MessagesListScreen(
            onConversationClick = onConversationClick,
            onProfileClick = onProfileClick,
        )
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Conversations List Pane (Fixed width ~360dp)
                Box(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxHeight(),
                ) {
                    MessagesListScreen(
                        onConversationClick = { id ->
                            selectedConversationId = id
                        },
                        onProfileClick = onProfileClick,
                    )
                }

                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Right Conversation Detail Pane
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    val activeId = selectedConversationId
                    if (activeId != null) {
                        MessageDetailScreen(
                            conversationId = activeId,
                            contactName = "Conversation $activeId",
                            contactAvatarUrl = "",
                            isOnline = true,
                            onBackClick = { selectedConversationId = null },
                            onProfileClick = onProfileClick,
                        )
                    } else {
                        EmptyState(
                            title = "Select a Conversation",
                            description = "Choose a chat from the list to start messaging.",
                            icon = Icons.Outlined.ChatBubbleOutline,
                        )
                    }
                }
            }
        }
    }
}
