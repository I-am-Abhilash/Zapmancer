package com.smach.zapmancer.domain.model

data class ConversationItem(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean = false,
    val isOnline: Boolean = false,
)
