package com.smach.zapmancer.presentation.auth.state

data class SignupUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
)
