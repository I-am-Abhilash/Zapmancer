package com.smach.zapmancer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Authentication + onboarding routing states */
sealed interface AppState {
    data object Loading : AppState
    data object Onboarding : AppState
    data object Unauthenticated : AppState
    data class Authenticated(val accessToken: String) : AppState
}

/** Central ViewModel that drives root UI routing */
class MainViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val appState: StateFlow<AppState> = combine(
        authRepository.getAccessToken(),
        authRepository.isOnboardingCompleted()
    ) { token, onboardingDone ->
        when {
            !onboardingDone -> AppState.Onboarding
            token.isNullOrBlank() -> AppState.Unauthenticated
            else -> AppState.Authenticated(token)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppState.Loading,
    )

    fun completeOnboarding() {
        viewModelScope.launch {
            authRepository.setOnboardingCompleted(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
