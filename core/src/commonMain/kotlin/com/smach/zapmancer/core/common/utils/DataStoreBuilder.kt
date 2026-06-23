package com.smach.zapmancer.core.common.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

// this project is currently on the datastore and migrate to sqldelight now the all the DataStoreStorage file has saveString and getString functions these are needed to migrate to sqldelight

expect fun createDataStore(): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "scriptside.preferences_pb"
