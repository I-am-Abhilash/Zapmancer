package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.database.dao.RecentSearchDao
import com.smach.zapmancer.core.database.entity.RecentSearchEntity
import com.smach.zapmancer.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class SearchRepositoryImpl(
    private val recentSearchDao: RecentSearchDao,
) : SearchRepository {

    override fun getTrendingTopics(): Flow<List<String>> {
        // In a real app, this would fetch from an API.
        // Here we return a flow of hardcoded strings to simulate the API response.
        return flowOf(listOf("Technology", "Design", "Business", "Lifestyle", "Health", "Science"))
    }

    override fun getRecentSearches(): Flow<List<String>> =
        recentSearchDao.getRecentSearches().map { entities ->
            entities.map { it.query }
        }

    override suspend fun saveSearchQuery(query: String) {
        if (query.isBlank()) return
        recentSearchDao.insertSearch(
            RecentSearchEntity(
                query = query.trim(),
                timestamp = Clock.System.now().toEpochMilliseconds(),
            ),
        )
        recentSearchDao.pruneOldSearches()
    }

    override suspend fun clearRecentSearches() {
        recentSearchDao.clearAll()
    }
}
