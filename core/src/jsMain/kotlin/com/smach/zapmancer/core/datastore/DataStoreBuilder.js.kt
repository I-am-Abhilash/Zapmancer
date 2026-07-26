package com.smach.zapmancer.core.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.annotation.Single

@Single
actual fun createDataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    produceFile = { DATA_STORE_FILE_NAME.toPath() }
)