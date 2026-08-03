package com.smach.zapmancer.presentation.settings.state

import com.smach.zapmancer.domain.model.SettingsData

data class SettingsUiState(
    val settings: SettingsData? = null,
    val isDarkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
