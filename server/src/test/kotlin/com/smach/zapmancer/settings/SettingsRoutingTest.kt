package com.smach.zapmancer.settings

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.SettingsData
import com.smach.zapmancer.settings.repository.SettingsField
import com.smach.zapmancer.settings.repository.SettingsRepository
import com.smach.zapmancer.settings.service.SettingsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SettingsRoutingTest {

    @Test
    fun testGetSettings() {
        runBlocking {
            val repo = mockk<SettingsRepository>()
            val service = SettingsService(repo)

            val settings = SettingsData(
                email = "alice@example.com",
                organization = "Zapmancer Inc",
                isTwoFactorEnabled = true,
                isEmailNotificationsEnabled = true,
                version = "1.0.0",
                isClientModeEnabled = false,
            )

            coEvery { repo.getSettings("usr_1") } returns settings

            val res = service.getSettings("usr_1")
            assertTrue(res.isTwoFactorEnabled)
            assertTrue(res.isEmailNotificationsEnabled)

            coEvery { repo.getSettings("usr_not_found") } returns null
            assertFailsWith<ApiException> {
                service.getSettings("usr_not_found")
            }
        }
    }

    @Test
    fun testToggles() {
        runBlocking {
            val repo = mockk<SettingsRepository>(relaxed = true)
            val service = SettingsService(repo)

            coEvery { repo.updateToggle("usr_1", SettingsField.TWO_FA, false) } returns true
            val r1 = service.toggle2fa("usr_1", false)
            assertTrue(r1.success)

            coEvery { repo.updateToggle("usr_1", SettingsField.EMAIL_NOTIFS, true) } returns true
            val r2 = service.toggleEmailNotifications("usr_1", true)
            assertTrue(r2.success)

            coEvery { repo.updateToggle("usr_1", SettingsField.CLIENT_MODE, true) } returns true
            val r3 = service.toggleClientMode("usr_1", true)
            assertTrue(r3.success)
        }
    }
}
