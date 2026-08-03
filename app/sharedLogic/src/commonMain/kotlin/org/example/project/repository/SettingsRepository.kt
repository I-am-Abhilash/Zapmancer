package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.SettingsData
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsData>
    val darkModeFlow: Flow<Boolean>

    suspend fun getSettings(): Result<SettingsData, DataError.Network>
    suspend fun updateTwoFactor(enabled: Boolean): Result<Unit, DataError.Network>
    suspend fun updateEmailNotifications(enabled: Boolean): Result<Unit, DataError.Network>
    suspend fun updateClientMode(enabled: Boolean): Result<Unit, DataError.Network>
    suspend fun updateDarkMode(enabled: Boolean)
}
