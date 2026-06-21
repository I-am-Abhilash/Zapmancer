package com.smach.zapmancer.features.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.usecase.GetUserProfileUseCase
import com.smach.zapmancer.domain.usecase.HireUserUseCase
import com.smach.zapmancer.features.profile.state.PortfolioItem
import com.smach.zapmancer.features.profile.state.ProfileReview
import com.smach.zapmancer.features.profile.state.ProfileUiState
import kotlinx.coroutines.launch

sealed class ProfileEvent {
    data object Refresh : ProfileEvent()
    data object ReviewMore : ProfileEvent()
    data object PortfolioMore : ProfileEvent()
    data object HireMe : ProfileEvent()
}

sealed class ProfileEffect {
    data class ShowToast(val message: String) : ProfileEffect()
}

class ProfileViewModel(
    private val userId: String? = null,
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
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = getUserProfileUseCase(userId)) {
                is Result.Success -> {
                    val profile = result.data
                    updateState {
                        copy(
                            name = profile.name,
                            role = profile.role,
                            location = profile.location,
                            ranking = profile.ranking,
                            isTopRated = profile.isTopRated,
                            projectsCount = profile.projectsCount,
                            rating = profile.rating,
                            experience = profile.experience,
                            about = profile.about,
                            skills = profile.skills,
                            portfolioItems = profile.portfolioItems.map {
                                PortfolioItem(
                                    title = it.title,
                                    description = it.description,
                                    imageUrl = it.imageUrl
                                )
                            },
                            reviews = profile.reviews.map {
                                ProfileReview(
                                    authorName = it.authorName,
                                    authorRole = it.authorRole,
                                    content = it.content,
                                    rating = it.rating,
                                    authorAvatarUrl = it.authorAvatarUrl.orEmpty()
                                )
                            },
                            avatarUrl = profile.avatarUrl.orEmpty(),
                            isLoading = false,
                            isOwnProfile = userId.isNullOrEmpty()
                        )
                    }
                }
                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Failed to load profile"
                        )
                    }
                }
            }
        }
    }

    private fun hireUser() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null, isHireSuccess = false) }
            when (val result = hireUserUseCase("julian_vancore")) {
                is Result.Success -> {
                    updateState { copy(isLoading = false, isHireSuccess = true) }
                    sendEffect(ProfileEffect.ShowToast("Hire request processed successfully!"))
                }
                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Failed to process hire request"
                        )
                    }
                    sendEffect(ProfileEffect.ShowToast("Failed to process hire request"))
                }
            }
        }
    }

    private fun onReviewMoreClick() {
        sendEffect(ProfileEffect.ShowToast("More reviews coming soon"))
    }

    private fun onLoadMorePortfolio() {
        sendEffect(ProfileEffect.ShowToast("More portfolio items coming soon"))
    }
}
