package com.smach.zapmancer.messages.data

import com.smach.zapmancer.core.common.dto.ConversationItem
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.database.ConversationsTable
import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.MessagesTable
import com.smach.zapmancer.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID
import kotlin.time.Clock.System

class MessagesRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private fun nowFormatted() = now().toString().take(16).replace("T", " ")

    suspend fun getConversations(userId: String): List<ConversationItem> = dbQuery {
        ConversationsTable.selectAll()
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
                    .where { MessagesTable.conversationId eq convId }
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
                    lastMessage = lastMsg?.get(MessagesTable.text) ?: "",
                    timestamp = lastMsg?.get(MessagesTable.createdAt)?.toString()?.take(16)
                        ?.replace("T", " ") ?: "",
                    isUnread = hasUnread,
                    isOnline = false, // Real presence requires WebSocket; return false for now
                )
            }
    }

    suspend fun getMessages(conversationId: String, userId: String): List<MessageItem> = dbQuery {
        MessagesTable.selectAll()
            .where { MessagesTable.conversationId eq conversationId }
            .orderBy(MessagesTable.createdAt, SortOrder.ASC)
            .map { row ->
                val senderId = row[MessagesTable.senderId]
                val sender =
                    UsersTable.selectAll().where { UsersTable.id eq senderId }.singleOrNull()
                MessageItem(
                    id = row[MessagesTable.id],
                    text = row[MessagesTable.text],
                    timestamp = row[MessagesTable.createdAt].toString().take(16).replace("T", " "),
                    isFromMe = senderId == userId,
                    status = row[MessagesTable.status],
                    avatarUrl = if (senderId == userId) null else sender?.get(UsersTable.avatarUrl),
                )
            }
    }

    suspend fun sendMessage(conversationId: String, senderId: String, text: String): String =
        dbQuery {
            val id = UUID.randomUUID().toString()
            MessagesTable.insert {
                it[MessagesTable.id] = id
                it[MessagesTable.conversationId] = conversationId
                it[MessagesTable.senderId] = senderId
                it[MessagesTable.text] = text
                it[MessagesTable.status] = "SENT"
                it[MessagesTable.createdAt] = now()
            }
            id
        }

    suspend fun markRead(conversationId: String, userId: String): Unit = dbQuery {
        MessagesTable.update({
            (MessagesTable.conversationId eq conversationId) and
                    (MessagesTable.senderId neq userId)
        }) {
            it[MessagesTable.status] = "READ"
        }
    }
}
