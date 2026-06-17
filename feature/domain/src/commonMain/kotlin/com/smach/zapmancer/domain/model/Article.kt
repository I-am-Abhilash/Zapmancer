package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val authorName: String,
    val authorAvatarUrl: String? = null,
    val thumbnailUrl: String? = null,
    val readingTimeMinutes: Int,
    val publishedDate: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val status: String = "PUBLISHED",
    val authorId: String = "",
    val isBookmarked: Boolean = false,
)


data class CreateArticle(
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val thumbnailUrl: String? = null,
)
