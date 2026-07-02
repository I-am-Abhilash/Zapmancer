package com.smach.zapmancer.messages.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.core.common.dto.*
import com.smach.zapmancer.messages.data.MessagesRepository

class MessagesService(private val repository: MessagesRepository) {

    suspend fun getConversations(userId: String): DomainResult<List<ConversationItem>> = DomainResult.Success(repository.getConversations(userId))

    suspend fun getMessages(conversationId: String, userId: String): DomainResult<List<MessageItem>> = DomainResult.Success(repository.getMessages(conversationId, userId))

    suspend fun sendMessage(conversationId: String, senderId: String, text: String): DomainResult<CommonResponse> {
        repository.sendMessage(conversationId, senderId, text)
        return DomainResult.Success(CommonResponse(success = true, message = "Message sent."))
    }

    suspend fun markRead(conversationId: String, userId: String): DomainResult<CommonResponse> {
        repository.markRead(conversationId, userId)
        return DomainResult.Success(CommonResponse(success = true, message = "Conversation marked as read."))
    }
}
