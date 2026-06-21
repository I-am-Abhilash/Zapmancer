package com.smach.zapmancer.features.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.usecase.ExportActivityCsvUseCase
import com.smach.zapmancer.domain.usecase.GetHomeDashboardUseCase
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.home.state.RecentActivity
import kotlinx.coroutines.launch

sealed class HomeEvent {
    data object LoadDashboard : HomeEvent()
    data object ExportCsv : HomeEvent()
}

sealed class HomeEffect {
    data class ShowToast(val message: String) : HomeEffect()
}

class HomeViewModel(
    private val getHomeDashboardUseCase: GetHomeDashboardUseCase,
    private val exportActivityCsvUseCase: ExportActivityCsvUseCase,
    private val settingsRepository: com.smach.zapmancer.domain.repository.SettingsRepository
) : BaseViewModel<HomeUiState, HomeEvent, HomeEffect>(HomeUiState()) {

    init {
        loadDashboard()
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                updateState { copy(isClientMode = settings.isClientModeEnabled) }
            }
        }
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadDashboard -> loadDashboard()
            HomeEvent.ExportCsv -> exportCsv()
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getHomeDashboardUseCase().fold(
                onSuccess = { dashboard ->
                    updateState {
                        copy(
                            userName = dashboard.userName,
                            totalEarnings = dashboard.earnings.amount,
                            earningsGrowth = dashboard.earnings.growthPercentage,
                            activeProjectsCount = dashboard.projectStats.activeCount,
                            totalCapacity = dashboard.projectStats.capacity,
                            systemRating = dashboard.systemRating,
                            recentActivities = dashboard.recentActivities.map { activity ->
                                RecentActivity(
                                    id = activity.id,
                                    projectName = activity.projectName,
                                    category = activity.category,
                                    categoryTag = activity.tag,
                                    status = activity.status,
                                    date = activity.timestamp,
                                    value = activity.monetaryValue
                                )
                            },
                            isLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    updateState { copy(isLoading = false) }
                    sendEffect(HomeEffect.ShowToast("Failed to load dashboard: ${exception.message}"))
                }
            )
        }
    }

    private fun exportCsv() {
        viewModelScope.launch {
            val activities = uiState.value.recentActivities.map {
                UserActivity(
                    id = it.id,
                    projectName = it.projectName,
                    category = it.category,
                    tag = it.categoryTag,
                    status = it.status,
                    timestamp = it.date,
                    monetaryValue = it.value
                )
            }
            exportActivityCsvUseCase(activities).fold(
                onSuccess = { filePath ->
                    sendEffect(HomeEffect.ShowToast("CSV exported to: $filePath"))
                },
                onFailure = { exception ->
                    sendEffect(HomeEffect.ShowToast("Export failed: ${exception.message}"))
                }
            )
        }
    }
}
