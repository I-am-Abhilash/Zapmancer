package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.features.auth.state.VerificationUiState
import kotlinx.coroutines.launch

sealed class VerificationEvent {
    data class CodeChanged(
        val code: String,
    ) : VerificationEvent()

    data object Submit : VerificationEvent()
}

class VerificationViewModel(
    private val authRepository: AuthRepository,
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
            // Simulating verification
            kotlinx.coroutines.delay(1000)
            updateState { copy(isLoading = false, isSuccess = true) }
        }
    }
}
