package com.smach.zapmancer.core.common.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.Single

@Single
actual fun createDataStore(): DataStore<Preferences> = throw UnsupportedOperationException("Datastore is not used on the Server target")
