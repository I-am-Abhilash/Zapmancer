package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.ForgotPasswordUseCase
import com.smach.zapmancer.features.auth.state.ForgotPasswordUiState
import kotlinx.coroutines.launch

sealed class ForgotPasswordEvent {
    data class EmailChanged(
        val email: String,
    ) : ForgotPasswordEvent()

    data object Submit : ForgotPasswordEvent()
}

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
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
            when (val result = forgotPasswordUseCase(currentState.email)) {
                is Result.Success -> {
                    updateState { copy(isLoading = false, isSuccess = true) }
                }

                is Result.Error -> {
                    updateState { copy(isLoading = false, error = result.error.toUserMessage()) }
                }
            }
        }
    }
}
