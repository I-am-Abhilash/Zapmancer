package com.smach.zapmancer.features.settings.state

data class SettingsUiState(
    val email: String = "admin@zapmancer.io",
    val organization: String = "Zapmancer Core Team",
    val isTwoFactorEnabled: Boolean = true,
    val isDarkModeEnabled: Boolean = true,
    val isEmailNotificationsEnabled: Boolean = false,
    val version: String = "v2.4.12-beta // ZAPMANCER_CORE_X64",
    val isLoading: Boolean = false
)
