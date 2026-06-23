package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.SettingsData
import com.smach.zapmancer.domain.repository.SettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

class SettingsRepositoryImpl(
    private val client: HttpClient,
) : SettingsRepository {

    private val _settingsFlow = MutableStateFlow(
        SettingsData(
            email = "admin@zapmancer.io",
            organization = "Zapmancer Core Team",
            isTwoFactorEnabled = true,
            isDarkModeEnabled = true,
            isEmailNotificationsEnabled = false,
            version = "v2.4.12-beta // ZAPMANCER_CORE_X64",
            isClientModeEnabled = false,
        ),
    )

    override val settingsFlow: Flow<SettingsData> = _settingsFlow.asStateFlow()

    override suspend fun getSettings(): SettingsData = when (
        val result = safeApiCall<SettingsData> {
            client.get("settings")
        }
    ) {
        is Result.Success -> {
            _settingsFlow.value = result.data
            result.data
        }

        is Result.Error -> {
            _settingsFlow.value
        }
    }

    override suspend fun updateTwoFactor(enabled: Boolean) = when (
        val result = safeApiCall<CommonResponse> {
            client.put("settings/2fa") {
                setBody(UpdateSettingRequest(enabled = enabled))
            }
        }
    ) {
        is Result.Success -> {
            _settingsFlow.value = _settingsFlow.value.copy(isTwoFactorEnabled = enabled)
        }

        is Result.Error -> {
            throw Exception("Failed to update 2FA: ${result.error}")
        }
    }

    override suspend fun updateDarkMode(enabled: Boolean) = when (
        val result = safeApiCall<CommonResponse> {
            client.put("settings/dark-mode") {
                setBody(UpdateSettingRequest(enabled = enabled))
            }
        }
    ) {
        is Result.Success -> {
            _settingsFlow.value = _settingsFlow.value.copy(isDarkModeEnabled = enabled)
        }

        is Result.Error -> {
            throw Exception("Failed to update dark mode: ${result.error}")
        }
    }

    override suspend fun updateEmailNotifications(enabled: Boolean) = when (
        val result = safeApiCall<CommonResponse> {
            client.put("settings/email-notifications") {
                setBody(UpdateSettingRequest(enabled = enabled))
            }
        }
    ) {
        is Result.Success -> {
            _settingsFlow.value =
                _settingsFlow.value.copy(isEmailNotificationsEnabled = enabled)
        }

        is Result.Error -> {
            throw Exception("Failed to update email notifications: ${result.error}")
        }
    }

    override suspend fun updateClientMode(enabled: Boolean) = when (
        val result = safeApiCall<CommonResponse> {
            client.put("settings/client-mode") {
                setBody(UpdateSettingRequest(enabled = enabled))
            }
        }
    ) {
        is Result.Success -> {
            _settingsFlow.value = _settingsFlow.value.copy(isClientModeEnabled = enabled)
        }

        is Result.Error -> {
            throw Exception("Failed to update client mode: ${result.error}")
        }
    }

    override suspend fun logout() {
        safeApiCall<CommonResponse> {
            client.post("auth/logout")
        }
    }
}

@Serializable
private data class UpdateSettingRequest(val enabled: Boolean)
