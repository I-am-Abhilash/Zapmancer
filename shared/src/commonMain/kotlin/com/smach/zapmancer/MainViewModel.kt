package com.smach.zapmancer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Authentication session states */
sealed interface SessionState {
    data object Loading : SessionState
    data object Unauthenticated : SessionState
    data class Authenticated(val accessToken: String) : SessionState
}

/** Central ViewModel that drives the UI */
class MainViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val session: StateFlow<SessionState> = authRepository.getAccessToken()
        .map { token ->
            if (token.isNullOrBlank()) {
                SessionState.Unauthenticated
            } else {
                SessionState.Authenticated(token)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState.Loading,
        )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
