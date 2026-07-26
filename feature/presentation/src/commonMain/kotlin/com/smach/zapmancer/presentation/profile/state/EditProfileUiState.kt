package com.smach.zapmancer.presentation.profile.state

data class EditProfileUiState(
    val name: String = "",
    val roleTitle: String = "",
    val location: String = "",
    val experience: String = "",
    val about: String = "",
    val skills: List<String> = emptyList(),
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
)
