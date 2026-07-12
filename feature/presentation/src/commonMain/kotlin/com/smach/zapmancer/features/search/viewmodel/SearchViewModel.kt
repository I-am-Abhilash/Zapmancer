package com.smach.zapmancer.features.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.features.search.state.SearchSortOption
import com.smach.zapmancer.features.search.state.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed interface SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent
    data object ClearQuery : SearchEvent
    data object StartVoiceSearch : SearchEvent
    data object StopVoiceSearch : SearchEvent
    data class AddRecentSearch(val query: String) : SearchEvent
    data class RemoveRecentSearch(val query: String) : SearchEvent
    data object ClearRecentSearches : SearchEvent
    data class ChangeCategoryFilter(val category: ProjectCategory) : SearchEvent
    data class ChangeSortOption(val sortOption: SearchSortOption) : SearchEvent
    data object Refresh : SearchEvent
    data object LoadNextPage : SearchEvent
    data class ProjectClicked(val id: String) : SearchEvent
    data object BackClicked : SearchEvent
}

sealed interface SearchEffect {
    data class ShowToast(val message: String) : SearchEffect
    data class NavigateToProjectDetail(val projectId: String) : SearchEffect
    data object NavigateBack : SearchEffect
}

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
) : BaseViewModel<SearchUiState, SearchEvent, SearchEffect>(SearchUiState()) {

    private val queryFlow = MutableStateFlow("")
    private var voiceSearchJob: Job? = null
    private var allFetchedProjects: List<Project> = emptyList()

    init {
        // Initialize recent searches
        updateState {
            copy(
                recentSearches = listOf("Smart Contract", "KMP Application", "Compose Multiplatform"),
            )
        }

        // Fetch initial project dataset
        loadInitialDataset()

        // Debounce text search changes
        viewModelScope.launch {
            queryFlow
                .debounce(300.milliseconds)
                .collect { debouncedQuery ->
                    executeSearch(debouncedQuery, resetPage = true)
                }
        }
    }

    override fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                updateState { copy(query = event.query) }
                queryFlow.value = event.query
            }

            SearchEvent.ClearQuery -> {
                updateState { copy(query = "") }
                queryFlow.value = ""
            }

            SearchEvent.StartVoiceSearch -> {
                startVoiceListening()
            }

            SearchEvent.StopVoiceSearch -> {
                voiceSearchJob?.cancel()
                updateState { copy(isListening = false) }
            }

            is SearchEvent.AddRecentSearch -> {
                addRecent(event.query)
            }

            is SearchEvent.RemoveRecentSearch -> {
                updateState {
                    copy(recentSearches = recentSearches - event.query)
                }
            }

            SearchEvent.ClearRecentSearches -> {
                updateState { copy(recentSearches = emptyList()) }
            }

            is SearchEvent.ChangeCategoryFilter -> {
                updateState { copy(categoryFilter = event.category) }
                executeSearch(uiState.value.query, resetPage = true)
            }

            is SearchEvent.ChangeSortOption -> {
                updateState { copy(sortBy = event.sortOption) }
                executeSearch(uiState.value.query, resetPage = true)
            }

            SearchEvent.Refresh -> {
                refreshData()
            }

            SearchEvent.LoadNextPage -> {
                loadNextPage()
            }

            is SearchEvent.ProjectClicked -> {
                addRecent(uiState.value.query)
                sendEffect(SearchEffect.NavigateToProjectDetail(event.id))
            }

            SearchEvent.BackClicked -> {
                sendEffect(SearchEffect.NavigateBack)
            }
        }
    }

    private fun loadInitialDataset() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getProjectsUseCase().foldTyped(
                onSuccess = { data ->
                    allFetchedProjects = data
                    executeSearch(uiState.value.query, resetPage = true)
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

    private fun executeSearch(query: String, resetPage: Boolean) {
        viewModelScope.launch {
            if (resetPage) {
                updateState { copy(isLoading = true, page = 1) }
            } else {
                updateState { copy(isLoading = true) }
            }

            if (query.isNotEmpty()) {
                delay(350.milliseconds)
            }

            val filtered = allFetchedProjects.filter { project ->
                val matchesQuery = query.isEmpty() ||
                    project.title.contains(query, ignoreCase = true) ||
                    project.description.contains(query, ignoreCase = true) ||
                    project.tags.any { it.contains(query, ignoreCase = true) }

                val matchesCategory = uiState.value.categoryFilter == ProjectCategory.ALL ||
                    project.category == uiState.value.categoryFilter

                matchesQuery && matchesCategory
            }

            val sorted = when (uiState.value.sortBy) {
                SearchSortOption.RELEVANCE -> {
                    filtered.sortedByDescending { project ->
                        var score = 0
                        if (project.title.contains(query, ignoreCase = true)) score += 10
                        if (project.description.contains(query, ignoreCase = true)) score += 3
                        project.tags.forEach { tag ->
                            if (tag.contains(query, ignoreCase = true)) score += 5
                        }
                        score
                    }
                }

                SearchSortOption.NEWEST -> {
                    filtered.sortedByDescending { it.id }
                }

                SearchSortOption.BUDGET -> {
                    filtered.sortedByDescending { it.membersCount }
                }
            }

            val pageSize = 5
            val currentPage = if (resetPage) 1 else uiState.value.page
            val itemsToShow = currentPage * pageSize
            val paginated = sorted.take(itemsToShow)
            val hasMore = sorted.size > paginated.size

            updateState {
                copy(
                    searchResults = paginated,
                    isLoading = false,
                    isRefreshing = false,
                    hasMore = hasMore,
                    page = currentPage,
                )
            }
        }
    }

    private fun addRecent(query: String) {
        if (query.isBlank()) return
        updateState {
            val list = (listOf(query) + recentSearches).distinct().take(8)
            copy(recentSearches = list)
        }
    }

    private fun startVoiceListening() {
        voiceSearchJob?.cancel()
        voiceSearchJob = viewModelScope.launch {
            updateState { copy(isListening = true) }
            delay(2500.milliseconds)
            val recognizedText = uiState.value.trendingSearches.random()
            updateState {
                copy(
                    query = recognizedText,
                    isListening = false,
                )
            }
            queryFlow.value = recognizedText
            addRecent(recognizedText)
            sendEffect(SearchEffect.ShowToast("Voice search matches: \"$recognizedText\""))
        }
    }

    private fun refreshData() {
        updateState { copy(isRefreshing = true) }
        viewModelScope.launch {
            getProjectsUseCase().foldTyped(
                onSuccess = { data ->
                    allFetchedProjects = data
                    executeSearch(uiState.value.query, resetPage = true)
                },
                onError = { error ->
                    updateState {
                        copy(
                            isRefreshing = false,
                            error = error.toUserMessage(),
                        )
                    }
                },
            )
        }
    }

    private fun loadNextPage() {
        if (uiState.value.isLoading || !uiState.value.hasMore) return
        val nextPage = uiState.value.page + 1
        updateState { copy(page = nextPage) }
        executeSearch(uiState.value.query, resetPage = false)
    }
}
