package com.smach.zapmancer.features.settings.state

import com.smach.zapmancer.domain.model.SettingsData

data class SettingsUiState(
    val settings: SettingsData? = null,
    val isLoading: Boolean = false,
)
