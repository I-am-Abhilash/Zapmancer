package com.smach.zapmancer.core.common.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createDataStore(): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "scriptside.preferences_pb"
