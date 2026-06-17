package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.features.auth.state.SignupUiState
import kotlinx.coroutines.launch

sealed class SignupEvent {
    data class EmailChanged(
        val email: String,
    ) : SignupEvent()

    data class UsernameChanged(
        val username: String,
    ) : SignupEvent()

    data class PasswordChanged(
        val password: String,
    ) : SignupEvent()

    object Submit : SignupEvent()
}

class SignupViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel<SignupUiState, SignupEvent, Unit>(SignupUiState()) {
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

            when (val result = authRepository.signUp(currentState.email, currentState.username, currentState.password)) {
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
