package com.smach.zapmancer.core.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File


class AndroidDataStoreBuilder : KoinComponent {
    private val context: Context by inject()

    fun create(): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { File(context.filesDir, DATA_STORE_FILE_NAME) },
    )
}

@Single
actual fun createDataStore(): DataStore<Preferences> = AndroidDataStoreBuilder().create()