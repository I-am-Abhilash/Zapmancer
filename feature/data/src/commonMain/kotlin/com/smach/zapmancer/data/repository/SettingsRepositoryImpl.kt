package com.smach.zapmancer.data.repository

import com.smach.zapmancer.domain.model.SettingsData
import com.smach.zapmancer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl : SettingsRepository {
    private val _settingsState = MutableStateFlow(
        SettingsData(
            email = "admin@zapmancer.io",
            organization = "Zapmancer Core Team",
            isTwoFactorEnabled = true,
            isDarkModeEnabled = true,
            isEmailNotificationsEnabled = false,
            version = "v2.4.12-beta // ZAPMANCER_CORE_X64"
        )
    )

    override val settingsFlow: Flow<SettingsData> = _settingsState.asStateFlow()

    override suspend fun getSettings(): SettingsData {
        return _settingsState.value
    }

    override suspend fun updateTwoFactor(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(isTwoFactorEnabled = enabled)
    }

    override suspend fun updateDarkMode(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(isDarkModeEnabled = enabled)
    }

    override suspend fun updateEmailNotifications(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(isEmailNotificationsEnabled = enabled)
    }

    override suspend fun logout() {
        // Simulated session cleanup
    }
}
