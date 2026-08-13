package com.smach.zapmancer.presentation.messages.screen

import androidx.compose.runtime.Composable

@Composable
fun MessagesAdaptiveScreen(
    onConversationClick: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
) {
    MessagesListScreen(
        onConversationClick = onConversationClick,
        onProfileClick = onProfileClick,
    )
}

