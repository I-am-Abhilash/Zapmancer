package com.smach.zapmancer.presentation.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.VerifyOtpUseCase
import com.smach.zapmancer.presentation.auth.state.VerificationUiState
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

sealed interface VerificationEvent {
    data class CodeChanged(
        val code: String,
    ) : VerificationEvent

    data object Submit : VerificationEvent
}

sealed interface VerificationEffect {
    data object NavigateToHome : VerificationEffect
}

@KoinViewModel
class VerificationViewModel(
    @InjectedParam private val email: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
) : BaseViewModel<VerificationUiState, VerificationEvent, VerificationEffect>(VerificationUiState()) {

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
            verifyOtpUseCase(email, currentState.code).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false, isSuccess = true) }
                    sendEffect(VerificationEffect.NavigateToHome)
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                },
            )
        }
    }
}
