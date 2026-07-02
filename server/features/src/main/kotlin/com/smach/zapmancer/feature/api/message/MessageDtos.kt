package com.smach.zapmancer.feature.api.message

import kotlinx.serialization.Serializable

@Serializable
data class ConversationItem(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean,
    val isOnline: Boolean,
)

@Serializable
data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    /** SENT | DELIVERED | READ */
    val status: String,
    val avatarUrl: String?,
)

@Serializable
data class SendMessageRequest(val text: String)
