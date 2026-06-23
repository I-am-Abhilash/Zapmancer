package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.smach.zapmancer.core.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreStorage(
    database: AppDatabase,
) {
    private val queries = database.appDatabaseQueries

    suspend fun saveString(
        key: String,
        value: String,
    ) {
        queries.insertKeyValue(key, value)
    }

    fun getString(key: String): Flow<String?> = queries.getValue(key)
        .asFlow()
        .mapToOneOrNull(Dispatchers.Default)
        .map { it?.value_ }
}
