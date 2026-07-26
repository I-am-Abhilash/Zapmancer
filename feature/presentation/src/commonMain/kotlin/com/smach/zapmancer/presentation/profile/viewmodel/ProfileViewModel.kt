package com.smach.zapmancer.presentation.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.GetUserProfileUseCase
import com.smach.zapmancer.domain.usecase.HireUserUseCase
import com.smach.zapmancer.presentation.profile.state.ProfileUiState
import com.smach.zapmancer.presentation.profile.state.toUiState
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

sealed interface ProfileEvent {
    data object Refresh : ProfileEvent
    data object ReviewMore : ProfileEvent
    data object PortfolioMore : ProfileEvent
    data object HireMe : ProfileEvent
    data object SearchClicked : ProfileEvent
    data object BackClicked : ProfileEvent
    data object EditProfileClicked : ProfileEvent
}

sealed interface ProfileEffect {
    data class ShowToast(val message: String) : ProfileEffect
    data object NavigateToSearch : ProfileEffect
    data object NavigateBack : ProfileEffect
    data object NavigateToEditProfile : ProfileEffect
}

@KoinViewModel
class ProfileViewModel(
    @InjectedParam private val userId: String? = null,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val hireUserUseCase: HireUserUseCase,
) : BaseViewModel<ProfileUiState, ProfileEvent, ProfileEffect>(ProfileUiState()) {

    init {
        loadProfile()
    }

    override fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Refresh -> loadProfile()
            ProfileEvent.ReviewMore -> onReviewMoreClick()
            ProfileEvent.PortfolioMore -> onLoadMorePortfolio()
            ProfileEvent.HireMe -> hireUser()
            ProfileEvent.SearchClicked -> sendEffect(ProfileEffect.NavigateToSearch)
            ProfileEvent.BackClicked -> sendEffect(ProfileEffect.NavigateBack)
            ProfileEvent.EditProfileClicked -> sendEffect(ProfileEffect.NavigateToEditProfile)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getUserProfileUseCase(userId).foldTyped(
                onSuccess = { profile ->
                    updateState {
                        profile.toUiState(isOwnProfile = userId.isNullOrEmpty())
                            .copy(isLoading = false)
                    }
                },
                onError = { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = error.toUserMessage(),
                        )
                    }
                },
            )
        }
    }

    private fun hireUser() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isHireSuccess = false) }
            hireUserUseCase(userId ?: "").foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false, isHireSuccess = true) }
                    sendEffect(ProfileEffect.ShowToast("Hire request processed successfully!"))
                },
                onError = { error ->
                    updateState { copy(isLoading = false) }
                    sendEffect(ProfileEffect.ShowToast("Failed to process hire request: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun onReviewMoreClick() {
        // TODO: implement pagination
        sendEffect(ProfileEffect.ShowToast("More reviews coming soon"))
    }

    private fun onLoadMorePortfolio() {
        // TODO: implement pagination
        sendEffect(ProfileEffect.ShowToast("More portfolio items coming soon"))
    }
}
