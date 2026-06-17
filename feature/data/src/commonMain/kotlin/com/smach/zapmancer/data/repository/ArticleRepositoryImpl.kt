package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.database.dao.ArticleDao
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.data.mapper.toDomain
import com.smach.zapmancer.data.mapper.toEntity
import com.smach.zapmancer.data.model.ArticleDto
import com.smach.zapmancer.data.model.CreateArticleDto
import com.smach.zapmancer.domain.model.Article
import com.smach.zapmancer.domain.model.CreateArticle
import com.smach.zapmancer.domain.repository.ArticleRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ArticleRepositoryImpl(
    private val httpClient: HttpClient,
    private val articleDao: ArticleDao,
) : ArticleRepository {

    override fun getTrendingArticles(): Flow<List<Article>> = flow {
        val cached = articleDao.getArticlesByFeed("TRENDING").map { it.map { it.toDomain() } }

        coroutineScope {
            launch {
                val result = safeApiCall<List<ArticleDto>> {
                    httpClient.get("articles/trending")
                }
                if (result is Result.Success) {
                    val bookmarkedIds = articleDao.getAllArticles().first()
                        .filter { it.isBookmarked }
                        .map { it.id }
                        .toSet()

                    val entities = result.data.map { dto ->
                        dto.toEntity("TRENDING").copy(
                            isBookmarked = bookmarkedIds.contains(dto.id)
                        )
                    }

                    articleDao.clearArticlesByFeed("TRENDING")
                    articleDao.insertArticles(entities)
                }
            }
            emitAll(cached)
        }
    }
    override fun getMyArticles(): Flow<List<Article>> = flow {
        val cached = articleDao.getArticlesByFeed("MY_ARTICLES").map { it.map { it.toDomain() } }

        coroutineScope {
            launch {
                val result = safeApiCall<List<ArticleDto>> {
                    httpClient.get("articles/me")
                }
                if (result is Result.Success) {
                    val bookmarkedIds = articleDao.getAllArticles().first()
                        .filter { it.isBookmarked }
                        .map { it.id }
                        .toSet()

                    val entities = result.data.map { dto ->
                        dto.toEntity("MY_ARTICLES").copy(
                            isBookmarked = bookmarkedIds.contains(dto.id)
                        )
                    }

                    articleDao.clearArticlesByFeed("MY_ARTICLES")
                    articleDao.insertArticles(entities)
                }
            }
            emitAll(cached)
        }
    }


    override fun getRecommendedArticles(): Flow<List<Article>> = flow {
        val cached = articleDao.getArticlesByFeed("RECOMMENDED").map { it.map { it.toDomain() } }

        coroutineScope {
            launch {
                val result = safeApiCall<List<ArticleDto>> {
                    httpClient.get("articles/recommended")
                }
                if (result is Result.Success) {
                    val bookmarkedIds = articleDao.getAllArticles().first()
                        .filter { it.isBookmarked }
                        .map { it.id }
                        .toSet()

                    val entities = result.data.map { dto ->
                        dto.toEntity("RECOMMENDED").copy(
                            isBookmarked = bookmarkedIds.contains(dto.id)
                        )
                    }

                    articleDao.clearArticlesByFeed("RECOMMENDED")
                    articleDao.insertArticles(entities)
                }
            }
            emitAll(cached)
        }
    }

    override fun getFollowingFeed(): Flow<List<Article>> = flow {
        val cached = articleDao.getArticlesByFeed("FEED").map { it.map { it.toDomain() } }

        coroutineScope {
            launch {
                val result = safeApiCall<List<ArticleDto>> {
                    httpClient.get("articles/feed")
                }
                if (result is Result.Success) {
                    val bookmarkedIds = articleDao.getAllArticles().first()
                        .filter { it.isBookmarked }
                        .map { it.id }
                        .toSet()

                    val entities = result.data.map { dto ->
                        dto.toEntity("FEED").copy(
                            isBookmarked = bookmarkedIds.contains(dto.id)
                        )
                    }

                    articleDao.clearArticlesByFeed("FEED")
                    articleDao.insertArticles(entities)
                }
            }
            emitAll(cached)
        }
    }

    override fun getArticleById(id: Int): Flow<Article> = flow {
        val cachedFlow = articleDao.getArticleById(id)
            .filterNotNull()
            .map { it.toDomain() }

        coroutineScope {
            launch {
                val result = safeApiCall<ArticleDto> {
                    httpClient.get("articles/$id")
                }
                if (result is Result.Success) {
                    val existing = articleDao.getArticleById(id).first()
                    val feedType = existing?.feedType ?: "DETAIL"
                    val isBookmarked = existing?.isBookmarked ?: false

                    val entity = result.data.toEntity(feedType).copy(isBookmarked = isBookmarked)
                    articleDao.insertArticles(listOf(entity))
                }
            }
            emitAll(cachedFlow)
        }
    }

    override fun searchArticles(query: String): Flow<List<Article>> = flow {
        val result = safeApiCall<List<ArticleDto>> {
            httpClient.get("articles") {
                parameter("q", query)
            }
        }
        if (result is Result.Success) {
            val searchResults = result.data.map { it.toDomain() }
            val bookmarkedFlow = articleDao.getAllArticles()
                .map { entities ->
                    entities.filter { it.isBookmarked }.map { it.id }.toSet()
                }

            emitAll(
                bookmarkedFlow.map { bookmarkedIds ->
                    searchResults.map { article ->
                        article.copy(isBookmarked = bookmarkedIds.contains(article.id))
                    }
                }
            )

        }
    }

    override suspend fun getArticlesPaged(
        page: Int,
        pageSize: Int,
    ): Result<List<Article>, DataError.Network> {
        val result = safeApiCall<List<ArticleDto>> {
            httpClient.get("articles") {
                parameter("page", page)
                parameter("size", pageSize)
            }
        }
        return when (result) {
            is Result.Success -> {
                val bookmarkedIds = articleDao.getAllArticles().first()
                    .filter { it.isBookmarked }
                    .map { it.id }
                    .toSet()

                val articles = result.data.map { dto ->
                    dto.toDomain().copy(isBookmarked = bookmarkedIds.contains(dto.id))
                }
                Result.Success(articles)
            }
            is Result.Error -> result
        }
    }

    override suspend fun getSearchArticlesPaged(
        query: String,
        page: Int,
        pageSize: Int,
    ): Result<List<Article>, DataError.Network> {
        val result = safeApiCall<List<ArticleDto>> {
            httpClient.get("articles") {
                parameter("q", query)
                parameter("page", page)
                parameter("size", pageSize)
            }
        }
        return when (result) {
            is Result.Success -> {
                val bookmarkedIds = articleDao.getAllArticles().first()
                    .filter { it.isBookmarked }
                    .map { it.id }
                    .toSet()

                val articles = result.data.map { dto ->
                    dto.toDomain().copy(isBookmarked = bookmarkedIds.contains(dto.id))
                }
                Result.Success(articles)
            }
            is Result.Error -> result
        }
    }

    override suspend fun createArticle(article: CreateArticle): Result<Unit, DataError.Network> =
        safeApiCall<Unit> {
            httpClient.post("articles/create") {
                setBody(
                    CreateArticleDto(
                        title = article.title,
                        content = article.content,
                        category = article.category,
                        tags = article.tags,
                        thumbnailUrl = article.thumbnailUrl,
                    ),
                )
            }
        }

    override suspend fun toggleBookmark(id: Int, isBookmarked: Boolean): Result<Unit, DataError.Network> {
        articleDao.updateBookmarkStatus(id, isBookmarked)
        return safeApiCall<Unit> {
            httpClient.post("articles/$id/bookmark")
        }
    }

    override suspend fun clapArticle(id: String): Result<Unit, DataError.Network> {
        return safeApiCall<Unit> {
            httpClient.post("articles/$id/clap")
        }
    }

    override suspend fun markArticleAsRead(id: String): Result<Unit, DataError.Network> {
        return safeApiCall<Unit> {
            httpClient.post("articles/$id/read")
        }
    }
}
