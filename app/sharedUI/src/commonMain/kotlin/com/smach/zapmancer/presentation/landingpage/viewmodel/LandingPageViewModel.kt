package com.smach.zapmancer.presentation.landingpage.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.GetLandingPageUseCase
import com.smach.zapmancer.presentation.landingpage.state.LandingPageUiState
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

sealed interface LandingPageEvent {
    data object LoadLandingPage : LandingPageEvent
    data class ToggleMegaMenu(val menuName: String?) : LandingPageEvent
    data object FindTalentClicked : LandingPageEvent
    data object FindWorkClicked : LandingPageEvent
    data object LoginClicked : LandingPageEvent
    data object SignUpClicked : LandingPageEvent
}

sealed interface LandingPageEffect {
    data class ShowToast(val message: String) : LandingPageEffect
    data object NavigateToSearchTalent : LandingPageEffect
    data object NavigateToBrowseProjects : LandingPageEffect
    data object NavigateToLogin : LandingPageEffect
    data object NavigateToSignUp : LandingPageEffect
}

@KoinViewModel
class LandingPageViewModel(
    private val getLandingPageUseCase: GetLandingPageUseCase,
) : BaseViewModel<LandingPageUiState, LandingPageEvent, LandingPageEffect>(LandingPageUiState()) {

    init {
        loadLandingPageData()
    }

    override fun onEvent(event: LandingPageEvent) {
        when (event) {
            LandingPageEvent.LoadLandingPage -> loadLandingPageData()
            is LandingPageEvent.ToggleMegaMenu -> {
                updateState {
                    copy(activeMegaMenu = if (activeMegaMenu == event.menuName) null else event.menuName)
                }
            }
            LandingPageEvent.FindTalentClicked -> sendEffect(LandingPageEffect.NavigateToSearchTalent)
            LandingPageEvent.FindWorkClicked -> sendEffect(LandingPageEffect.NavigateToBrowseProjects)
            LandingPageEvent.LoginClicked -> sendEffect(LandingPageEffect.NavigateToLogin)
            LandingPageEvent.SignUpClicked -> sendEffect(LandingPageEffect.NavigateToSignUp)
        }
    }

    private fun loadLandingPageData() {
        updateState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getLandingPageUseCase().foldTyped(
                onSuccess = { landingData ->
                    updateState { copy(isLoading = false, data = landingData) }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                },
            )
        }
    }
}
