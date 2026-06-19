package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.Article
import com.smach.zapmancer.domain.model.CreateArticle
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getTrendingArticles(): Flow<List<Article>>

    fun getRecommendedArticles(): Flow<List<Article>>

    fun getArticleById(id: Int): Flow<Article>

    fun searchArticles(query: String): Flow<List<Article>>

    fun getFollowingFeed(): Flow<List<Article>>

    suspend fun getArticlesPaged(page: Int, pageSize: Int): Result<List<Article>, DataError.Network>

    suspend fun getSearchArticlesPaged(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<Article>, DataError.Network>

    suspend fun createArticle(article: CreateArticle): Result<Unit, DataError.Network>

    suspend fun toggleBookmark(id: Int, isBookmarked: Boolean): Result<Unit, DataError.Network>

    suspend fun clapArticle(id: String): Result<Unit, DataError.Network>

    suspend fun markArticleAsRead(id: String): Result<Unit, DataError.Network>

    fun getMyArticles(): Flow<List<Article>>
}
