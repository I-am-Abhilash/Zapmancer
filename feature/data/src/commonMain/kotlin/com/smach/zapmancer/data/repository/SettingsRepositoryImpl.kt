package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.SettingsData as SettingsDataDto
import com.smach.zapmancer.core.common.dto.ToggleRequest
import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.SettingsData
import com.smach.zapmancer.domain.repository.SettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val client: HttpClient,
) : SettingsRepository {

    // Start with sensible defaults so the UI has something to render before the
    // first network response. Network responses replace these — we never silently
    // fall back to fake seed values on error.
    private val _settingsFlow = MutableStateFlow(SettingsData())
    override val settingsFlow: Flow<SettingsData> = _settingsFlow.asStateFlow()

    override suspend fun getSettings(): Result<SettingsData, DataError.Network> {
        val result = safeApiCall<SettingsDataDto> { client.get("settings") }
        return when (result) {
            is Result.Success -> {
                val domainData = result.data.toDomain(isDarkMode = _settingsFlow.value.isDarkModeEnabled)
                _settingsFlow.value = domainData
                Result.Success(domainData)
            }
            is Result.Error -> result
        }
    }

    override suspend fun updateTwoFactor(enabled: Boolean): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.put("settings/2fa") {
            setBody(ToggleRequest(enabled = enabled))
        }
    }.also { result ->
        if (result is Result.Success) {
            _settingsFlow.value = _settingsFlow.value.copy(isTwoFactorEnabled = enabled)
        }
    }.toUnitResult()

    override suspend fun updateEmailNotifications(enabled: Boolean): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.put("settings/email-notifications") {
            setBody(ToggleRequest(enabled = enabled))
        }
    }.also { result ->
        if (result is Result.Success) {
            _settingsFlow.value = _settingsFlow.value.copy(isEmailNotificationsEnabled = enabled)
        }
    }.toUnitResult()

    override suspend fun updateClientMode(enabled: Boolean): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.put("settings/client-mode") {
            setBody(ToggleRequest(enabled = enabled))
        }
    }.also { result ->
        if (result is Result.Success) {
            _settingsFlow.value = _settingsFlow.value.copy(isClientModeEnabled = enabled)
        }
    }.toUnitResult()
}

private fun SettingsDataDto.toDomain(isDarkMode: Boolean): SettingsData = SettingsData(
    email = email,
    organization = organization.orEmpty(),
    isTwoFactorEnabled = isTwoFactorEnabled,
    isDarkModeEnabled = isDarkMode,
    isEmailNotificationsEnabled = isEmailNotificationsEnabled,
    version = version,
    isClientModeEnabled = isClientModeEnabled,
)
