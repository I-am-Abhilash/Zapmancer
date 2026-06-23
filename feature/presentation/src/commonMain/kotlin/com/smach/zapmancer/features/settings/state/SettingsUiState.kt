package com.smach.zapmancer.features.settings.state

import com.smach.zapmancer.domain.model.SettingsData

data class SettingsUiState(
    val settings : List<SettingsData> = emptyList(),
    val isLoading: Boolean = false,
)


