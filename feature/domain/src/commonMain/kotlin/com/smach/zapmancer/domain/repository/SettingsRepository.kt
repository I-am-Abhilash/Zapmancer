package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.domain.model.SettingsData
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsData>
    suspend fun getSettings(): SettingsData
    suspend fun updateTwoFactor(enabled: Boolean)
    suspend fun updateDarkMode(enabled: Boolean)
    suspend fun updateEmailNotifications(enabled: Boolean)
    suspend fun logout()
}
