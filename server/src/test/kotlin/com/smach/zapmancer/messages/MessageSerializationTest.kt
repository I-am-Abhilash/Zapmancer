package com.smach.zapmancer.messages

import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.core.common.dto.MessageReactionItem
import com.smach.zapmancer.messages.redis.RedisChatEnvelope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MessageSerializationTest {

    @Test
    fun testClientToServerFramesSerialization() {
        val json = Json { ignoreUnknownKeys = true }

        // Test SendMessage frame with attachment
        val sendFrame: ChatFrame.ClientToServer = ChatFrame.ClientToServer.SendMessage(
            conversationId = "conv-1",
            text = "Check this contract",
            attachmentUrl = "https://cdn.zapmancer.com/contracts/contract.pdf",
            attachmentType = "FILE",
            attachmentName = "contract.pdf",
            attachmentSizeBytes = 1048576,
            replyToMessageId = "msg-0"
        )
        val sendEncoded = json.encodeToString(sendFrame)
        assertNotNull(sendEncoded)
        assertTrue(sendEncoded.contains("contract.pdf"))

        val decodedSend = json.decodeFromString<ChatFrame.ClientToServer>(sendEncoded)
        assertTrue(decodedSend is ChatFrame.ClientToServer.SendMessage)
        assertEquals("conv-1", (decodedSend as ChatFrame.ClientToServer.SendMessage).conversationId)

        // Test React frame
        val reactFrame: ChatFrame.ClientToServer = ChatFrame.ClientToServer.React(
            conversationId = "conv-1",
            messageId = "msg-123",
            emoji = "🔥"
        )
        val reactEncoded = json.encodeToString(reactFrame)
        val decodedReact = json.decodeFromString<ChatFrame.ClientToServer>(reactEncoded)
        assertTrue(decodedReact is ChatFrame.ClientToServer.React)
        assertEquals("🔥", (decodedReact as ChatFrame.ClientToServer.React).emoji)
    }

    @Test
    fun testServerToClientFramesSerialization() {
        val json = Json { ignoreUnknownKeys = true }

        val messageItem = MessageItem(
            id = "msg-1",
            text = "Hello there!",
            timestamp = "2026-08-17 15:30",
            isFromMe = true,
            status = "READ",
            avatarUrl = "https://avatar.png",
            attachmentUrl = "https://img.png",
            attachmentType = "IMAGE",
            reactions = listOf(MessageReactionItem(emoji = "👍", count = 3, isMine = true)),
            isEdited = true,
            isDeleted = false,
            readAt = "2026-08-17 15:31"
        )

        val newMsgFrame: ChatFrame.ServerToClient = ChatFrame.ServerToClient.NewMessage(
            conversationId = "conv-1",
            message = messageItem
        )
        val encoded = json.encodeToString(newMsgFrame)
        assertTrue(encoded.contains("👍"))

        val decoded = json.decodeFromString<ChatFrame.ServerToClient>(encoded)
        assertTrue(decoded is ChatFrame.ServerToClient.NewMessage)
        val decodedMsg = (decoded as ChatFrame.ServerToClient.NewMessage).message
        assertEquals(1, decodedMsg.reactions.size)
        assertEquals("👍", decodedMsg.reactions.first().emoji)
        assertTrue(decodedMsg.isEdited)
    }

    @Test
    fun testRedisEnvelopeSerialization() {
        val json = Json { ignoreUnknownKeys = true }

        val envelope = RedisChatEnvelope(
            targetUserId = "user-456",
            frameJson = """{"conversationId":"c1","isTyping":true}"""
        )
        val encoded = json.encodeToString(envelope)
        val decoded = json.decodeFromString<RedisChatEnvelope>(encoded)

        assertEquals("user-456", decoded.targetUserId)
        assertTrue(decoded.frameJson.contains("isTyping"))
    }
}
