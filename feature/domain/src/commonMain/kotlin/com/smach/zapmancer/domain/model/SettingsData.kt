package com.smach.zapmancer.domain.model

data class SettingsData(
    val email: String,
    val organization: String,
    val isTwoFactorEnabled: Boolean,
    val isDarkModeEnabled: Boolean,
    val isEmailNotificationsEnabled: Boolean,
    val version: String
)
