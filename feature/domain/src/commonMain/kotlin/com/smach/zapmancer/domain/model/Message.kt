package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val status: MessageStatus = MessageStatus.SENT,
    val avatarUrl: String? = null
)

@Serializable
enum class MessageStatus {
    SENT, DELIVERED, READ
}
