package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val email: String,
    val organization: String,
    val isTwoFactorEnabled: Boolean,
    val isDarkModeEnabled: Boolean,
    val isEmailNotificationsEnabled: Boolean,
    val version: String,
    val isClientModeEnabled: Boolean = false,
)
