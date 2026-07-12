package com.smach.zapmancer.domain.model

data class SettingsData(
    val email: String = "",
    val organization: String = "",
    val isTwoFactorEnabled: Boolean = false,
    val isEmailNotificationsEnabled: Boolean = false,
    val version: String = "",
    val isClientModeEnabled: Boolean = false,
)
