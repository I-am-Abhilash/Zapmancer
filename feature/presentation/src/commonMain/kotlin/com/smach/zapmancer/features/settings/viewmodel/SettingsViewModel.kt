package com.smach.zapmancer.features.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.usecase.GetSettingsUseCase
import com.smach.zapmancer.domain.usecase.LogoutUseCase
import com.smach.zapmancer.domain.usecase.UpdateSettingsUseCase
import com.smach.zapmancer.features.settings.state.SettingsUiState
import kotlinx.coroutines.launch

sealed class SettingsEvent {
    data class ToggleTwoFactor(val enabled: Boolean) : SettingsEvent()
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()
    data class ToggleEmailNotifications(val enabled: Boolean) : SettingsEvent()
    data object Logout : SettingsEvent()
    data object LoadSettings : SettingsEvent()
}

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<SettingsUiState, SettingsEvent, Unit>(SettingsUiState()) {

    init {
        loadSettings()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleTwoFactor -> toggleTwoFactor(event.enabled)
            is SettingsEvent.ToggleDarkMode -> toggleDarkMode(event.enabled)
            is SettingsEvent.ToggleEmailNotifications -> toggleNotifications(event.enabled)
            SettingsEvent.Logout -> logout()
            SettingsEvent.LoadSettings -> loadSettings()
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getSettingsUseCase().fold(
                onSuccess = { settings ->
                    updateState {
                        copy(
                            email = settings.email,
                            organization = settings.organization,
                            isTwoFactorEnabled = settings.isTwoFactorEnabled,
                            isDarkModeEnabled = settings.isDarkModeEnabled,
                            isEmailNotificationsEnabled = settings.isEmailNotificationsEnabled,
                            version = settings.version,
                            isLoading = false
                        )
                    }
                },
                onFailure = {
                    updateState { copy(isLoading = false) }
                }
            )
        }
    }

    private fun toggleTwoFactor(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateTwoFactor(enabled).fold(
                onSuccess = {
                    updateState { copy(isTwoFactorEnabled = enabled) }
                },
                onFailure = {}
            )
        }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateDarkMode(enabled).fold(
                onSuccess = {
                    updateState { copy(isDarkModeEnabled = enabled) }
                },
                onFailure = {}
            )
        }
    }

    private fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateEmailNotifications(enabled).fold(
                onSuccess = {
                    updateState { copy(isEmailNotificationsEnabled = enabled) }
                },
                onFailure = {}
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
