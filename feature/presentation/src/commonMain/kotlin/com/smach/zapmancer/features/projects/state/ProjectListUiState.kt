package com.smach.zapmancer.features.projects.state

import com.smach.zapmancer.domain.model.Article

data class ProjectListUiState(
    val id: Int = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaging: Boolean = false,
    val trendingArticles: List<Article> = emptyList(),
    val recommendedArticles: List<Article> = emptyList(),
    val categories: List<String> = listOf(
        "For You",
        "Following",
        "Technology",
        "Design",
        "Writing",
        "Programming",
    ),
    val selectedCategory: String = "For You",
    val page: Int = 1,
    val endReached: Boolean = false,
    val error: String? = null,
    val title: String = "Home",
)