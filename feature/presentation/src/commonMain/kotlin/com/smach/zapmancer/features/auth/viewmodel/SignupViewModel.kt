package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.SignUpUseCase
import com.smach.zapmancer.features.auth.state.SignupUiState
import kotlinx.coroutines.launch

sealed interface SignupEvent {
    data class EmailChanged(
        val email: String,
    ) : SignupEvent

    data class UsernameChanged(
        val username: String,
    ) : SignupEvent

    data class PasswordChanged(
        val password: String,
    ) : SignupEvent

    data object Submit : SignupEvent
}

sealed interface SignupEffect {
    data class NavigateToVerification(val email: String) : SignupEffect
}

class SignupViewModel(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignupUiState, SignupEvent, SignupEffect>(SignupUiState()) {

    override fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EmailChanged -> updateState { copy(email = event.email) }
            is SignupEvent.UsernameChanged -> updateState { copy(username = event.username) }
            is SignupEvent.PasswordChanged -> updateState { copy(password = event.password) }
            SignupEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val currentState = uiState.value
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            signUpUseCase(
                currentState.email,
                currentState.username,
                currentState.password,
            ).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false, isSuccess = true) }
                    sendEffect(SignupEffect.NavigateToVerification(currentState.email))
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                },
            )
        }
    }
}
