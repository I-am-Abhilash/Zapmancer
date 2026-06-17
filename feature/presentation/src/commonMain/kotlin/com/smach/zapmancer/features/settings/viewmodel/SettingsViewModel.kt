package com.smach.zapmancer.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.smach.zapmancer.features.settings.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        // Handle events
    }
}

sealed class SettingsEvent {
    data class ToggleTwoFactor(val enabled: Boolean) : SettingsEvent()
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()
    data class ToggleEmailNotifications(val enabled: Boolean) : SettingsEvent()
}
