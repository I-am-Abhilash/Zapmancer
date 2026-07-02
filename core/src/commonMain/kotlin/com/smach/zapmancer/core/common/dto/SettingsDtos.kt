package com.smach.zapmancer.core.common.dto

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
