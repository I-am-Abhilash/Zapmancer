package com.smach.zapmancer.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = "articles",
    primaryKeys = ["id", "feedType"],
)
data class ArticleEntity(
    val id: Int,
    val feedType: String, // e.g., "TRENDING", "RECOMMENDED", "SEARCH", "PAGED"
    val title: String,
    val content: String,
    val authorName: String,
    val authorAvatarUrl: String?,
    val thumbnailUrl: String?,
    val readingTimeMinutes: Int,
    val publishedDate: String,
    val category: String,
    val likesCount: Int,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val status: String = "PUBLISHED",
    val authorId: String = "",
    val isBookmarked: Boolean,
)
