package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.VerifyOtpUseCase
import com.smach.zapmancer.features.auth.state.VerificationUiState
import kotlinx.coroutines.launch

sealed class VerificationEvent {
    data class CodeChanged(
        val code: String,
    ) : VerificationEvent()

    data object Submit : VerificationEvent()
}

class VerificationViewModel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
) : BaseViewModel<VerificationUiState, VerificationEvent, Unit>(VerificationUiState()) {
    override fun onEvent(event: VerificationEvent) {
        when (event) {
            is VerificationEvent.CodeChanged -> updateState { copy(code = event.code) }
            VerificationEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val currentState = uiState.value
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            // Assuming we pass current email/context or dummy for now.
            // In a real application, email is passed down from previous screens or saved in session/state.
            val email = "user@example.com" 
            when (val result = verifyOtpUseCase(email, currentState.code)) {
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
