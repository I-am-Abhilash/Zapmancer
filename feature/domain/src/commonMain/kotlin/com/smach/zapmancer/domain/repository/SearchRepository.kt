package com.smach.zapmancer.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getTrendingTopics(): Flow<List<String>>
    fun getRecentSearches(): Flow<List<String>>
    suspend fun saveSearchQuery(query: String)
    suspend fun clearRecentSearches()
}
