package com.smach.zapmancer.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.smach.zapmancer.features.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        // Handle events
    }
}

sealed class ProfileEvent {
    object Refresh : ProfileEvent()
    object HireMe : ProfileEvent()
}
