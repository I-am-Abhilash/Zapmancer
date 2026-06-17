package com.smach.zapmancer.features.auth.state

data class VerificationUiState(
    val code: String = "000000",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)
