package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val email: String = "",
    val organization: String = "",
    val isTwoFactorEnabled: Boolean = false,
    val isDarkModeEnabled: Boolean = false,
    val isEmailNotificationsEnabled: Boolean = false,
    val version: String = "",
    val isClientModeEnabled: Boolean = false,
)