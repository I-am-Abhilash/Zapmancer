package com.smach.zapmancer.presentation.search.state

import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory

enum class SearchSortOption(val displayName: String) {
    RELEVANCE("Most Relevant"),
    NEWEST("Newest"),
    BUDGET("Highest Budget"),
}

data class SearchUiState(
    val query: String = "",
    val isListening: Boolean = false,
    val recentSearches: List<String> = emptyList(),
    val trendingSearches: List<String> = listOf(
        "Compose Multiplatform",
        "Smart Contract",
        "KMP Application",
        "Material 3 Design System",
        "WebAssembly (Wasm)",
        "Rust Backend integration",
    ),
    val categoryFilter: ProjectCategory = ProjectCategory.ALL,
    val sortBy: SearchSortOption = SearchSortOption.RELEVANCE,
    val searchResults: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false,
    val page: Int = 1,
)
