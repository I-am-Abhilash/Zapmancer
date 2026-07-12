package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.ForgotPasswordUseCase
import com.smach.zapmancer.features.auth.state.ForgotPasswordUiState
import kotlinx.coroutines.launch

import com.smach.zapmancer.core.common.utils.foldTyped

sealed interface ForgotPasswordEvent {
    data class EmailChanged(
        val email: String,
    ) : ForgotPasswordEvent

    data object Submit : ForgotPasswordEvent
}

sealed interface ForgotPasswordEffect {
    data object NavigateToLogin : ForgotPasswordEffect
}

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordEvent, ForgotPasswordEffect>(ForgotPasswordUiState()) {

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
            forgotPasswordUseCase(currentState.email).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false, isSuccess = true) }
                    sendEffect(ForgotPasswordEffect.NavigateToLogin)
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                }
            )
        }
    }
}
