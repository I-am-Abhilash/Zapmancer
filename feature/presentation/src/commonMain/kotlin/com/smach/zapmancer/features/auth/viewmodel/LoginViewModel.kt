package com.smach.zapmancer.features.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.features.auth.state.LoginUiState
import kotlinx.coroutines.launch

sealed class LoginEvent {
    data class OnEmailChanged(
        val email: String,
    ) : LoginEvent()

    data class OnPasswordChanged(
        val password: String,
    ) : LoginEvent()

    data class OnRememberMeChanged(
        val isChecked: Boolean,
    ) : LoginEvent()

    data object OnForgotPasswordClicked : LoginEvent()

    data object OnTogglePasswordVisibility : LoginEvent()

    data object OnRegisterHereClicked : LoginEvent()

    data object Submit : LoginEvent()
}

sealed class LoginSideEffect {
    data class NavigateToOtp(
        val email: String,
    ) : LoginSideEffect()

    data object NavigateToForgotPassword : LoginSideEffect()
}

class LoginViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginUiState, LoginEvent, Unit>(LoginUiState()) {
    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> updateState { copy(email = event.email) }
            is LoginEvent.OnPasswordChanged -> updateState { copy(password = event.password) }
            is LoginEvent.OnRememberMeChanged -> TODO()
            LoginEvent.OnTogglePasswordVisibility -> updateState { copy(togglePassword = !togglePassword) }
            LoginEvent.Submit -> submit()
            LoginEvent.OnForgotPasswordClicked -> TODO()
            LoginEvent.OnRegisterHereClicked -> TODO()
        }
    }

    private fun submit() {
        val currentState = uiState.value
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            when (val result = authRepository.login(currentState.email, currentState.password)) {
                is Result.Success -> {
                    updateState { copy(isLoading = false, isSuccess = true) }
                }

                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = result.error.toUserMessage(),
                        )
                    }
                }
            }
        }
    }
}
