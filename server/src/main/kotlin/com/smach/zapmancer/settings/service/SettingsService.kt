package com.smach.zapmancer.settings.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.SettingsData
import com.smach.zapmancer.settings.repository.SettingsField
import com.smach.zapmancer.settings.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SettingsService(private val repository: SettingsRepository) {

    suspend fun getSettings(userId: String): SettingsData = repository.getSettings(userId)
        ?: throw ApiException(ErrorCode.NOT_FOUND, "User not found.")

    suspend fun toggle2fa(userId: String, enabled: Boolean): CommonResponse {
        repository.updateToggle(userId, SettingsField.TWO_FA, enabled)
        return CommonResponse(
            success = true,
            message = "2FA updated successfully.",
        )
    }

    suspend fun toggleEmailNotifications(
        userId: String,
        enabled: Boolean,
    ): CommonResponse {
        repository.updateToggle(userId, SettingsField.EMAIL_NOTIFS, enabled)
        return CommonResponse(
            success = true,
            message = "Weekly updates subscription toggled.",
        )
    }

    suspend fun toggleClientMode(userId: String, enabled: Boolean): CommonResponse {
        repository.updateToggle(userId, SettingsField.CLIENT_MODE, enabled)
        return CommonResponse(
            success = true,
            message = "Workspace layout toggled successfully.",
        )
    }
}
