package com.smach.zapmancer.features.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.ExportActivityCsvUseCase
import com.smach.zapmancer.domain.usecase.GetHomeDashboardUseCase
import com.smach.zapmancer.domain.usecase.GetUserProfileUseCase
import com.smach.zapmancer.features.home.state.HomeUiState
import kotlinx.coroutines.launch

sealed interface HomeEvent {
    data object LoadDashboard : HomeEvent
    data object ExportCsv : HomeEvent
    data object ProfileClicked : HomeEvent
    data object CompleteProfileClicked : HomeEvent
    data object SearchClicked : HomeEvent
    data object CreateProjectClicked : HomeEvent
}

sealed interface HomeEffect {
    data class ShowToast(val message: String) : HomeEffect
    data object NavigateToProfile : HomeEffect
    data object NavigateToCompleteProfile : HomeEffect
    data object NavigateToSearch : HomeEffect
    data object NavigateToCreateProject : HomeEffect
}

class HomeViewModel(
    private val getHomeDashboardUseCase: GetHomeDashboardUseCase,
    private val exportActivityCsvUseCase: ExportActivityCsvUseCase,
    private val settingsRepository: com.smach.zapmancer.domain.repository.SettingsRepository,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : BaseViewModel<HomeUiState, HomeEvent, HomeEffect>(HomeUiState()) {

    init {
        loadDashboard()
        observeSettings()
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadDashboard -> loadDashboard()
            HomeEvent.ExportCsv -> exportCsv()
            HomeEvent.ProfileClicked -> sendEffect(HomeEffect.NavigateToProfile)
            HomeEvent.CompleteProfileClicked -> sendEffect(HomeEffect.NavigateToCompleteProfile)
            HomeEvent.SearchClicked -> sendEffect(HomeEffect.NavigateToSearch)
            HomeEvent.CreateProjectClicked -> sendEffect(HomeEffect.NavigateToCreateProject)
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                updateState { copy(isClientMode = settings.isClientModeEnabled) }
            }
        }
    }

    private fun loadDashboard() {
        updateState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getUserProfileUseCase(null).foldTyped(
                onSuccess = { profile ->
                    val incomplete = profile.name.isBlank() ||
                        profile.role.isBlank() ||
                        profile.location.orEmpty().isBlank() ||
                        profile.about.orEmpty().isBlank() ||
                        profile.skills.isEmpty()
                    updateState { copy(showCompleteProfileBanner = incomplete) }
                },
                onError = {
                    // Fail silently for dashboard onboarding alert
                },
            )
        }

        viewModelScope.launch {
            getHomeDashboardUseCase().foldTyped(
                onSuccess = { dashboard ->
                    updateState {
                        copy(
                            userName = dashboard.userName,
                            totalEarnings = "$" + dashboard.earnings.amount.toString(),
                            earningsGrowth = (if (dashboard.earnings.growthPercentage >= 0) "+" else "") + dashboard.earnings.growthPercentage.toString() + "%",
                            activeProjectsCount = dashboard.projectStats.activeCount,
                            totalCapacity = dashboard.projectStats.capacity,
                            systemRating = dashboard.systemRating,
                            // Populate client-mode dashboard stats stubs (H-5)
                            totalSpent = "$" + dashboard.earnings.amount.toString(),
                            activeJobPostsCount = dashboard.projectStats.activeCount,
                            proposalsReceivedCount = 0,
                            recentActivities = dashboard.recentActivities,
                            isLoading = false,
                        )
                    }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(HomeEffect.ShowToast("Failed to load dashboard: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun exportCsv() {
        viewModelScope.launch {
            val activities = uiState.value.recentActivities
            exportActivityCsvUseCase(activities).foldTyped(
                onSuccess = { filePath ->
                    sendEffect(HomeEffect.ShowToast("CSV exported to: $filePath"))
                },
                onError = { error ->
                    sendEffect(HomeEffect.ShowToast("Export failed: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
