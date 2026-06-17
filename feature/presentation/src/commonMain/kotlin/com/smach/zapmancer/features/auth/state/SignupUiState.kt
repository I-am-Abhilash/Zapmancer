package com.smach.zapmancer.features.auth.state

data class SignupUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
)
