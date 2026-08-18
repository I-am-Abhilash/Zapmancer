package com.smach.zapmancer.notifications

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.NotificationAction
import com.smach.zapmancer.core.common.dto.NotificationItem
import com.smach.zapmancer.notifications.repository.NotificationsRepository
import com.smach.zapmancer.notifications.service.NotificationsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NotificationsRoutingTest {

    @Test
    fun testGetNotificationsFeed() {
        runBlocking {
            val repo = mockk<NotificationsRepository>()
            val service = NotificationsService(repo)

            val item = NotificationItem(
                id = 1,
                type = "MILESTONE",
                title = "Proposal Accepted",
                description = "You have been hired for project.",
                timestamp = "10m ago",
                section = "Today",
                codeSnippet = null,
                isItalic = false,
                actions = listOf(NotificationAction(label = "VIEW_CONTRACT", isPrimary = true, isError = false)),
                quickReply = false,
            )

            coEvery { repo.getNotifications("usr_freelancer") } returns listOf(item)

            val notifications = service.getNotifications("usr_freelancer")
            assertEquals(1, notifications.size)
            assertEquals("Proposal Accepted", notifications[0].title)
        }
    }

    @Test
    fun testExecuteAction() {
        runBlocking {
            val repo = mockk<NotificationsRepository>()
            val service = NotificationsService(repo)

            coEvery { repo.markAsRead(1, "usr_1") } returns true

            val res = service.executeAction(1, "usr_1", "VIEW_CONTRACT")
            assertTrue(res.success)
            assertEquals("Contract review opened.", res.message)

            val res2 = service.executeAction(1, "usr_1", "ACCEPT_OFFER")
            assertTrue(res2.success)
            assertEquals("Offer accepted successfully.", res2.message)

            assertFailsWith<ApiException> {
                service.executeAction(1, "usr_1", "")
            }
        }
    }

    @Test
    fun testSendQuickReply() {
        runBlocking {
            val repo = mockk<NotificationsRepository>()
            val service = NotificationsService(repo)

            coEvery { repo.markAsRead(1, "usr_1") } returns true

            val res = service.sendQuickReply(1, "usr_1", "Sounds great, on it!")
            assertTrue(res.success)

            assertFailsWith<ApiException> {
                service.sendQuickReply(1, "usr_1", "  ")
            }
        }
    }

    @Test
    fun testReadAndReadAll() {
        runBlocking {
            val repo = mockk<NotificationsRepository>()
            val service = NotificationsService(repo)

            coEvery { repo.markAsRead(10, "usr_1") } returns true
            val r1 = service.markAsRead(10, "usr_1")
            assertTrue(r1.success)

            coEvery { repo.markAllAsRead("usr_1") } returns true
            val rAll = service.markAllAsRead("usr_1")
            assertTrue(rAll.success)
        }
    }
}
