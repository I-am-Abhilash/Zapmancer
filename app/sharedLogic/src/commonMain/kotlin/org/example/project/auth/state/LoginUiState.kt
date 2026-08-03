package com.smach.zapmancer.presentation.auth.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val togglePassword: Boolean = true,
    val isRememberMe: Boolean = true,
    val error: String? = null,
    val isSuccess: Boolean = false,
)
