package com.smach.zapmancer.domain.model

data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val status: MessageStatus = MessageStatus.SENT,
    val avatarUrl: String? = null,
)

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ,
}
