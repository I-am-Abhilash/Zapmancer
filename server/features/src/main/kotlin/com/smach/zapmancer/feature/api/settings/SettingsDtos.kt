package com.smach.zapmancer.feature.api.settings

import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val email: String,
    val organization: String?,
    val isTwoFactorEnabled: Boolean,
    val isEmailNotificationsEnabled: Boolean,
    val version: String,
    val isClientModeEnabled: Boolean,
)

@Serializable
data class ToggleRequest(val enabled: Boolean)
