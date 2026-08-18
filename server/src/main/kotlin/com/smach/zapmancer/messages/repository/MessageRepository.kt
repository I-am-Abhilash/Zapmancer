package com.smach.zapmancer.messages.repository

import com.smach.zapmancer.core.common.dto.ConversationItem
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.core.common.dto.MessageReactionItem
import com.smach.zapmancer.core.database.ConversationsTable
import com.smach.zapmancer.core.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.core.database.MessageReactionsTable
import com.smach.zapmancer.core.database.MessagesTable
import com.smach.zapmancer.core.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID
import kotlin.time.Clock.System

class MessageRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    suspend fun getConversations(userId: String): List<ConversationItem> = dbQuery {
        ConversationsTable
            .selectAll()
            .where { (ConversationsTable.user1Id eq userId) or (ConversationsTable.user2Id eq userId) }
            .map { row ->
                val convId = row[ConversationsTable.id]
                val otherId = if (row[ConversationsTable.user1Id] == userId) {
                    row[ConversationsTable.user2Id]
                } else {
                    row[ConversationsTable.user1Id]
                }

                val otherUser = UsersTable.selectAll()
                    .where { UsersTable.id eq otherId }.singleOrNull()

                val lastMsg = MessagesTable.selectAll()
                    .where { (MessagesTable.conversationId eq convId) and (MessagesTable.isDeleted eq false) }
                    .orderBy(MessagesTable.createdAt, SortOrder.DESC)
                    .limit(1)
                    .singleOrNull()

                val hasUnread = lastMsg != null &&
                    lastMsg[MessagesTable.senderId] != userId &&
                    lastMsg[MessagesTable.status] != "READ"

                ConversationItem(
                    id = convId,
                    name = otherUser?.get(UsersTable.username) ?: "Unknown",
                    avatarUrl = otherUser?.get(UsersTable.avatarUrl),
                    lastMessage = lastMsg?.get(MessagesTable.text)
                        ?: if (lastMsg?.get(MessagesTable.attachmentUrl) != null) "[Attachment]" else "",
                    timestamp = lastMsg?.get(MessagesTable.createdAt)?.toString()?.take(16)
                        ?.replace("T", " ") ?: "",
                    isUnread = hasUnread,
                    isOnline = false,
                )
            }
    }

    suspend fun getMessages(
        conversationId: String,
        userId: String,
        limit: Int = 50,
    ): List<MessageItem> = dbQuery {
        val messages = MessagesTable
            .selectAll()
            .where { MessagesTable.conversationId eq conversationId }
            .orderBy(MessagesTable.createdAt, SortOrder.ASC)
            .limit(limit)
            .toList()

        messages.map { row ->
            val msgId = row[MessagesTable.id]
            val senderId = row[MessagesTable.senderId]
            val sender = UsersTable.selectAll().where { UsersTable.id eq senderId }.singleOrNull()

            // Fetch reply snippet if this message is a reply
            val replyId = row[MessagesTable.replyToMessageId]
            val replySnippet = if (replyId != null) {
                MessagesTable.selectAll().where { MessagesTable.id eq replyId }
                    .singleOrNull()?.get(MessagesTable.text)?.take(50)
            } else {
                null
            }

            val reactions = fetchReactionsForMessage(msgId, userId)

            val isDeleted = row[MessagesTable.isDeleted]
            MessageItem(
                id = msgId,
                text = if (isDeleted) "This message was deleted" else row[MessagesTable.text],
                timestamp = row[MessagesTable.createdAt].toString().take(16).replace("T", " "),
                isFromMe = senderId == userId,
                status = row[MessagesTable.status],
                avatarUrl = if (senderId == userId) null else sender?.get(UsersTable.avatarUrl),
                attachmentUrl = if (isDeleted) null else row[MessagesTable.attachmentUrl],
                attachmentType = if (isDeleted) null else row[MessagesTable.attachmentType],
                attachmentName = if (isDeleted) null else row[MessagesTable.attachmentName],
                attachmentSizeBytes = if (isDeleted) null else row[MessagesTable.attachmentSizeBytes],
                replyToMessageId = replyId,
                replyToSnippet = replySnippet,
                reactions = reactions,
                isEdited = row[MessagesTable.isEdited],
                isDeleted = isDeleted,
                readAt = row[MessagesTable.readAt]?.toString()?.take(16)?.replace("T", " "),
            )
        }
    }

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        text: String,
        attachmentUrl: String? = null,
        attachmentType: String? = null,
        attachmentName: String? = null,
        attachmentSizeBytes: Long? = null,
        replyToMessageId: String? = null,
    ): String = dbQuery {
        val id = UUID.randomUUID().toString()
        MessagesTable.insert {
            it[MessagesTable.id] = id
            it[MessagesTable.conversationId] = conversationId
            it[MessagesTable.senderId] = senderId
            it[MessagesTable.text] = text
            it[MessagesTable.status] = "SENT"
            it[MessagesTable.attachmentUrl] = attachmentUrl
            it[MessagesTable.attachmentType] = attachmentType
            it[MessagesTable.attachmentName] = attachmentName
            it[MessagesTable.attachmentSizeBytes] = attachmentSizeBytes
            it[MessagesTable.replyToMessageId] = replyToMessageId
            it[MessagesTable.isEdited] = false
            it[MessagesTable.isDeleted] = false
            it[MessagesTable.createdAt] = now()
        }
        id
    }

    suspend fun toggleReaction(
        messageId: String,
        userId: String,
        emoji: String,
    ): List<MessageReactionItem> = dbQuery {
        val existing = MessageReactionsTable.selectAll()
            .where {
                (MessageReactionsTable.messageId eq messageId) and
                    (MessageReactionsTable.userId eq userId) and
                    (MessageReactionsTable.emoji eq emoji)
            }
            .singleOrNull()

        if (existing != null) {
            MessageReactionsTable.deleteWhere {
                (MessageReactionsTable.messageId eq messageId) and
                    (MessageReactionsTable.userId eq userId) and
                    (MessageReactionsTable.emoji eq emoji)
            }
        } else {
            MessageReactionsTable.insert {
                it[MessageReactionsTable.messageId] = messageId
                it[MessageReactionsTable.userId] = userId
                it[MessageReactionsTable.emoji] = emoji
                it[MessageReactionsTable.createdAt] = now()
            }
        }

        fetchReactionsForMessage(messageId, userId)
    }

    suspend fun editMessage(
        messageId: String,
        senderId: String,
        newText: String,
    ): Boolean = dbQuery {
        val updatedRows = MessagesTable.update({
            (MessagesTable.id eq messageId) and (MessagesTable.senderId eq senderId) and (MessagesTable.isDeleted eq false)
        }) {
            it[MessagesTable.text] = newText
            it[MessagesTable.isEdited] = true
        }
        updatedRows > 0
    }

    suspend fun deleteMessage(
        messageId: String,
        senderId: String,
    ): Boolean = dbQuery {
        val updatedRows = MessagesTable.update({
            (MessagesTable.id eq messageId) and (MessagesTable.senderId eq senderId)
        }) {
            it[MessagesTable.isDeleted] = true
            it[MessagesTable.text] = ""
            it[MessagesTable.attachmentUrl] = null
        }
        updatedRows > 0
    }

    suspend fun markMessageRead(messageId: String, readerId: String): String? = dbQuery {
        val msg = MessagesTable.selectAll()
            .where { (MessagesTable.id eq messageId) and (MessagesTable.senderId neq readerId) }
            .singleOrNull() ?: return@dbQuery null

        val currentTime = now()
        MessagesTable.update({ MessagesTable.id eq messageId }) {
            it[MessagesTable.status] = "READ"
            it[MessagesTable.readAt] = currentTime
        }
        msg[MessagesTable.senderId]
    }

    suspend fun markAllRead(conversationId: String, userId: String): Unit = dbQuery {
        val currentTime = now()
        MessagesTable.update({
            (MessagesTable.conversationId eq conversationId) and
                (MessagesTable.senderId neq userId) and
                (MessagesTable.status neq "READ")
        }) {
            it[MessagesTable.status] = "READ"
            it[MessagesTable.readAt] = currentTime
        }
    }

    suspend fun getConversationParticipants(conversationId: String): Pair<String, String>? = dbQuery {
        ConversationsTable.selectAll()
            .where { ConversationsTable.id eq conversationId }
            .map { row ->
                Pair(row[ConversationsTable.user1Id], row[ConversationsTable.user2Id])
            }
            .singleOrNull()
    }

    suspend fun getMessage(messageId: String, userId: String): MessageItem? = dbQuery {
        MessagesTable.selectAll()
            .where { MessagesTable.id eq messageId }
            .map { row ->
                val senderId = row[MessagesTable.senderId]
                val sender = UsersTable.selectAll().where { UsersTable.id eq senderId }.singleOrNull()

                val replyId = row[MessagesTable.replyToMessageId]
                val replySnippet = if (replyId != null) {
                    MessagesTable.selectAll().where { MessagesTable.id eq replyId }
                        .singleOrNull()?.get(MessagesTable.text)?.take(50)
                } else {
                    null
                }

                val reactions = fetchReactionsForMessage(messageId, userId)
                val isDeleted = row[MessagesTable.isDeleted]

                MessageItem(
                    id = row[MessagesTable.id],
                    text = if (isDeleted) "This message was deleted" else row[MessagesTable.text],
                    timestamp = row[MessagesTable.createdAt].toString().take(16).replace("T", " "),
                    isFromMe = senderId == userId,
                    status = row[MessagesTable.status],
                    avatarUrl = if (senderId == userId) null else sender?.get(UsersTable.avatarUrl),
                    attachmentUrl = if (isDeleted) null else row[MessagesTable.attachmentUrl],
                    attachmentType = if (isDeleted) null else row[MessagesTable.attachmentType],
                    attachmentName = if (isDeleted) null else row[MessagesTable.attachmentName],
                    attachmentSizeBytes = if (isDeleted) null else row[MessagesTable.attachmentSizeBytes],
                    replyToMessageId = replyId,
                    replyToSnippet = replySnippet,
                    reactions = reactions,
                    isEdited = row[MessagesTable.isEdited],
                    isDeleted = isDeleted,
                    readAt = row[MessagesTable.readAt]?.toString()?.take(16)?.replace("T", " "),
                )
            }.singleOrNull()
    }

    private fun fetchReactionsForMessage(messageId: String, currentUserId: String): List<MessageReactionItem> {
        val rows = MessageReactionsTable.selectAll()
            .where { MessageReactionsTable.messageId eq messageId }
            .toList()

        return rows.groupBy { it[MessageReactionsTable.emoji] }
            .map { (emoji, list) ->
                val userIds = list.map { it[MessageReactionsTable.userId] }
                MessageReactionItem(
                    emoji = emoji,
                    count = list.size,
                    userIds = userIds,
                    isMine = userIds.contains(currentUserId),
                )
            }
    }
}
