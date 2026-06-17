//package com.smach.zapmancer.features.home.viewmodel
//
//import androidx.lifecycle.viewModelScope
//import com.smach.zapmancer.core.common.base.BaseViewModel
//import com.smach.zapmancer.core.common.utils.DefaultPaginator
//import com.smach.zapmancer.core.common.utils.toUserMessage
//import com.smach.zapmancer.domain.repository.ArticleRepository
//import com.smach.zapmancer.features.home.state.HomeUiState
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//sealed class HomeEvent {
//    object LoadNextArticles : HomeEvent()
//    object Refresh : HomeEvent()
//    data class SelectCategory(val category: String) : HomeEvent()
//}
//
//class HomeViewModel(
//    private val articleRepository: ArticleRepository,
//) : BaseViewModel<HomeUiState, HomeEvent, Unit>(HomeUiState()) {
//
//    private var trendingJob: Job? = null
//
//    private val paginator =
//        DefaultPaginator(
//            initialKey = 1,
//            onLoadUpdated = { updateState { copy(isPaging = it) } },
//            onRequest = { nextKey -> articleRepository.getArticlesPaged(nextKey, 20) },
//            getNextKey = { uiState.value.page + 1 },
//            onError = { error, _ -> updateState { copy(error = error.toUserMessage()) } },
//            onSuccess = { items, newKey ->
//                updateState {
//                    copy(
//                        recommendedArticles = recommendedArticles + items,
//                        page = newKey,
//                        endReached = items.isEmpty(),
//                    )
//                }
//            },
//        )
//
//    init {
//        refresh()
//    }
//
//    private fun refresh() {
//        viewModelScope.launch {
//            updateState { copy(isRefreshing = true) }
//
//            // 1. Refresh Trending Articles (observe updates)
//            trendingJob?.cancel()
//            trendingJob = viewModelScope.launch {
//                articleRepository.getTrendingArticles().collectLatest { trending ->
//                    updateState { copy(trendingArticles = trending, isLoading = false) }
//                }
//            }
//
//            // 2. Refresh Recommended Articles (Paginator)
//            paginator.reset()
//            updateState { copy(recommendedArticles = emptyList(), page = 1, endReached = false) }
//            paginator.loadNextItems()
//
//            updateState { copy(isRefreshing = false) }
//        }
//    }
//
//    fun loadNextItems() {
//        viewModelScope.launch {
//            paginator.loadNextItems()
//        }
//    }
//
//    override fun onEvent(event: HomeEvent) {
//        when (event) {
//            HomeEvent.LoadNextArticles -> {
//                loadNextItems()
//            }
//
//            HomeEvent.Refresh -> {
//                refresh()
//            }
//
//            is HomeEvent.SelectCategory -> {
//                updateState { copy(selectedCategory = event.category) }
//            }
//        }
//    }
//}
