package com.smach.zapmancer.features.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.domain.usecase.GetSettingsUseCase
import com.smach.zapmancer.domain.usecase.LogoutUseCase
import com.smach.zapmancer.domain.usecase.UpdateTwoFactorUseCase
import com.smach.zapmancer.domain.usecase.UpdateEmailNotificationsUseCase
import com.smach.zapmancer.domain.usecase.UpdateClientModeUseCase
import com.smach.zapmancer.domain.usecase.GetDarkModeUseCase
import com.smach.zapmancer.domain.usecase.UpdateDarkModeUseCase
import com.smach.zapmancer.features.settings.state.SettingsUiState
import kotlinx.coroutines.launch
import com.smach.zapmancer.core.common.utils.toUserMessage

sealed interface SettingsEvent {
    data class ToggleTwoFactor(val enabled: Boolean) : SettingsEvent
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent
    data class ToggleEmailNotifications(val enabled: Boolean) : SettingsEvent
    data class ToggleClientMode(val enabled: Boolean) : SettingsEvent
    data object Logout : SettingsEvent
    data object BackClicked : SettingsEvent
}

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
    data object NavigateToLogin : SettingsEffect
    data class ShowToast(val message: String) : SettingsEffect
}

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateTwoFactorUseCase: UpdateTwoFactorUseCase,
    private val updateEmailNotificationsUseCase: UpdateEmailNotificationsUseCase,
    private val updateClientModeUseCase: UpdateClientModeUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val updateDarkModeUseCase: UpdateDarkModeUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsEffect>(SettingsUiState()) {

    init {
        loadSettings()
        observeDarkMode()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleTwoFactor -> toggleTwoFactor(event.enabled)
            is SettingsEvent.ToggleDarkMode -> toggleDarkMode(event.enabled)
            is SettingsEvent.ToggleEmailNotifications -> toggleNotifications(event.enabled)
            is SettingsEvent.ToggleClientMode -> toggleClientMode(event.enabled)
            SettingsEvent.Logout -> logout()
            SettingsEvent.BackClicked -> sendEffect(SettingsEffect.NavigateBack)
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getSettingsUseCase().foldTyped(
                onSuccess = { settings ->
                    updateState { copy(isLoading = false, settings = settings) }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(SettingsEffect.ShowToast("Failed to load settings: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun observeDarkMode() {
        viewModelScope.launch {
            getDarkModeUseCase().collect { isDark ->
                updateState { copy(isDarkModeEnabled = isDark) }
            }
        }
    }

    private fun toggleTwoFactor(enabled: Boolean) {
        viewModelScope.launch {
            updateTwoFactorUseCase(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isTwoFactorEnabled = enabled)) } },
                onError = { error ->
                    sendEffect(SettingsEffect.ShowToast("Failed to toggle 2FA: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            updateDarkModeUseCase(enabled)
        }
    }

    private fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            updateEmailNotificationsUseCase(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isEmailNotificationsEnabled = enabled)) } },
                onError = { error ->
                    sendEffect(SettingsEffect.ShowToast("Failed to toggle notifications: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun toggleClientMode(enabled: Boolean) {
        viewModelScope.launch {
            updateClientModeUseCase(enabled).foldTyped(
                onSuccess = { updateState { copy(settings = settings?.copy(isClientModeEnabled = enabled)) } },
                onError = { error ->
                    sendEffect(SettingsEffect.ShowToast("Failed to toggle client mode: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase().foldTyped(
                onSuccess = { sendEffect(SettingsEffect.NavigateToLogin) },
                onError = { sendEffect(SettingsEffect.NavigateToLogin) },
            )
        }
    }
}
