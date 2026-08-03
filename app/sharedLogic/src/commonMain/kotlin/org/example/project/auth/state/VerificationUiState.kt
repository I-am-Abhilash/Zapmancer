package com.smach.zapmancer.presentation.auth.state

data class VerificationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)
