package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.features.auth.state.ForgotPasswordUiState
import kotlinx.coroutines.launch

sealed class ForgotPasswordEvent {
    data class EmailChanged(
        val email: String,
    ) : ForgotPasswordEvent()

    data object Submit : ForgotPasswordEvent()
}

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordEvent, Unit>(ForgotPasswordUiState()) {
    override fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> updateState { copy(email = event.email) }
            ForgotPasswordEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val currentState = uiState.value
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            // Mocking forgot password call since it might not be in repository yet or using login as placeholder
            // For now let's just simulate success after a delay
            kotlinx.coroutines.delay(1000)
            updateState { copy(isLoading = false, isSuccess = true) }
        }
    }
}
