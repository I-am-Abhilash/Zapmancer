package com.smach.zapmancer.messages

import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.core.common.dto.MessageReactionItem
import com.smach.zapmancer.messages.repository.MessageRepository
import com.smach.zapmancer.messages.service.ConnectionManager
import com.smach.zapmancer.messages.service.MessageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MessageWorkflowTest {

    private val repository = mockk<MessageRepository>(relaxed = true)
    private val connectionManager = mockk<ConnectionManager>(relaxed = true)

    private val messageService = MessageService(
        repository = repository,
        connectionManager = connectionManager,
    )

    @Test
    fun testSendMessageDispatchesToBothParticipants() = runBlocking {
        coEvery {
            repository.sendMessage(
                conversationId = "conv_1",
                senderId = "user_A",
                text = "Hello B!",
                attachmentUrl = null,
                attachmentType = null,
                attachmentName = null,
                attachmentSizeBytes = null,
                replyToMessageId = null,
            )
        } returns "msg_100"

        coEvery { repository.getConversationParticipants("conv_1") } returns Pair("user_A", "user_B")

        val sampleMsg = MessageItem(
            id = "msg_100",
            text = "Hello B!",
            timestamp = "2026-08-17 17:00",
            isFromMe = true,
            status = "SENT",
            avatarUrl = null,
        )
        coEvery { repository.getMessage("msg_100", any()) } returns sampleMsg

        val response = messageService.sendMessage(
            conversationId = "conv_1",
            senderId = "user_A",
            text = "Hello B!",
        )

        assertTrue(response.success)
        coVerify(exactly = 1) { connectionManager.sendToUser("user_A", any<ChatFrame.ServerToClient.NewMessage>()) }
        coVerify(exactly = 1) { connectionManager.sendToUser("user_B", any<ChatFrame.ServerToClient.NewMessage>()) }
    }

    @Test
    fun testHandleTypingUpdatesConnectionManagerAndRecipient() = runBlocking {
        coEvery { repository.getConversationParticipants("conv_1") } returns Pair("user_A", "user_B")

        messageService.handleTyping("conv_1", "user_A", isTyping = true)

        coVerify(exactly = 1) { connectionManager.setTyping("conv_1", "user_A", true) }
        coVerify(exactly = 1) {
            connectionManager.sendToUser(
                "user_B",
                match<ChatFrame.ServerToClient.TypingUpdate> { it.isTyping && it.userId == "user_A" },
            )
        }
    }

    @Test
    fun testHandleReactDispatchesReactionUpdateToBothUsers() = runBlocking {
        val reactions = listOf(MessageReactionItem(emoji = "🔥", count = 1, isMine = true))
        coEvery { repository.toggleReaction("msg_100", "user_A", "🔥") } returns reactions
        coEvery { repository.getConversationParticipants("conv_1") } returns Pair("user_A", "user_B")

        messageService.handleReact("conv_1", "user_A", "msg_100", "🔥")

        coVerify(exactly = 1) { repository.toggleReaction("msg_100", "user_A", "🔥") }
        coVerify(exactly = 1) { connectionManager.sendToUser("user_A", any<ChatFrame.ServerToClient.ReactionUpdate>()) }
        coVerify(exactly = 1) { connectionManager.sendToUser("user_B", any<ChatFrame.ServerToClient.ReactionUpdate>()) }
    }

    @Test
    fun testEditMessageDispatchesEditedFrame() = runBlocking {
        coEvery { repository.editMessage("msg_100", "user_A", "Edited text") } returns true
        coEvery { repository.getConversationParticipants("conv_1") } returns Pair("user_A", "user_B")

        messageService.handleEditMessage("conv_1", "user_A", "msg_100", "Edited text")

        coVerify(exactly = 1) { repository.editMessage("msg_100", "user_A", "Edited text") }
        coVerify(exactly = 1) {
            connectionManager.sendToUser(
                "user_A",
                match<ChatFrame.ServerToClient.MessageEdited> { it.newText == "Edited text" },
            )
        }
    }

    @Test
    fun testDeleteMessageDispatchesDeletedFrame() = runBlocking {
        coEvery { repository.deleteMessage("msg_100", "user_A") } returns true
        coEvery { repository.getConversationParticipants("conv_1") } returns Pair("user_A", "user_B")

        messageService.handleDeleteMessage("conv_1", "user_A", "msg_100")

        coVerify(exactly = 1) { repository.deleteMessage("msg_100", "user_A") }
        coVerify(exactly = 1) {
            connectionManager.sendToUser(
                "user_B",
                match<ChatFrame.ServerToClient.MessageDeleted> { it.isDeleted },
            )
        }
    }

    @Test
    fun testMarkMessageReadUpdatesStatusAndNotifiesSender() = runBlocking {
        coEvery { repository.markMessageRead("msg_100", "user_B") } returns "user_A"

        val response = messageService.markMessageRead("conv_1", "user_B", "msg_100")

        assertTrue(response.success)
        coVerify(exactly = 1) {
            connectionManager.sendToUser(
                "user_A",
                match<ChatFrame.ServerToClient.MessageStatusUpdate> { it.status == "READ" },
            )
        }
    }
}
