package com.smach.zapmancer.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single


@Single
class DataStoreStorage(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun saveString(
        key: String,
        value: String,
    ) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getString(key: String): Flow<String?> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }
}
