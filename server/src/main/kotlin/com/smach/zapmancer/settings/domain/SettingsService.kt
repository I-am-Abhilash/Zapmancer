package com.smach.zapmancer.settings.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.ErrorCode
import com.smach.zapmancer.core.common.dto.SettingsData
import com.smach.zapmancer.settings.data.SettingsField
import com.smach.zapmancer.settings.data.SettingsRepository

class SettingsService(private val repository: SettingsRepository) {

    suspend fun getSettings(userId: String): DomainResult<SettingsData> {
        val data = repository.getSettings(userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "User not found.")
        return DomainResult.Success(data)
    }

    suspend fun toggle2fa(userId: String, enabled: Boolean): DomainResult<CommonResponse> {
        repository.updateToggle(userId, SettingsField.TWO_FA, enabled)
        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "2FA updated successfully."
            )
        )
    }

    suspend fun toggleEmailNotifications(
        userId: String,
        enabled: Boolean
    ): DomainResult<CommonResponse> {
        repository.updateToggle(userId, SettingsField.EMAIL_NOTIFS, enabled)
        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "Weekly updates subscription toggled."
            )
        )
    }

    suspend fun toggleClientMode(userId: String, enabled: Boolean): DomainResult<CommonResponse> {
        repository.updateToggle(userId, SettingsField.CLIENT_MODE, enabled)
        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "Workspace layout toggled successfully."
            )
        )
    }
}
