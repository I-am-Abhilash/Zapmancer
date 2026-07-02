package com.smach.zapmancer.features.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.domain.usecase.GetSettingsUseCase
import com.smach.zapmancer.domain.usecase.LogoutUseCase
import com.smach.zapmancer.domain.usecase.UpdateSettingsUseCase
import com.smach.zapmancer.features.settings.state.SettingsUiState
import kotlinx.coroutines.launch

sealed class SettingsEvent {
    data class ToggleTwoFactor(val enabled: Boolean) : SettingsEvent()
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()
    data class ToggleEmailNotifications(val enabled: Boolean) : SettingsEvent()
    data class ToggleClientMode(val enabled: Boolean) : SettingsEvent()
    data object Logout : SettingsEvent()
    data object LoadSettings : SettingsEvent()
}

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, Unit>(SettingsUiState()) {

    init {
        loadSettings()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleTwoFactor -> toggleTwoFactor(event.enabled)
            is SettingsEvent.ToggleDarkMode -> toggleDarkMode(event.enabled)
            is SettingsEvent.ToggleEmailNotifications -> toggleNotifications(event.enabled)
            is SettingsEvent.ToggleClientMode -> toggleClientMode(event.enabled)
            SettingsEvent.Logout -> logout()
            SettingsEvent.LoadSettings -> loadSettings()
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getSettingsUseCase().foldTyped(
                onSuccess = { settings ->
                    updateState { copy(isLoading = false, settings = settings) }
                },
                onError = {
                    updateState { copy(isLoading = false) }
                },
            )
        }
    }

    private fun toggleTwoFactor(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateTwoFactor(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isTwoFactorEnabled = enabled)) } },
                onError = { /* keep prior state; user can retry */ },
            )
        }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateDarkMode(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isDarkModeEnabled = enabled)) } },
                onError = { },
            )
        }
    }

    private fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateEmailNotifications(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isEmailNotificationsEnabled = enabled)) } },
                onError = { },
            )
        }
    }

    private fun toggleClientMode(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateClientMode(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isClientModeEnabled = enabled)) } },
                onError = { },
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
