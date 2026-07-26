package com.smach.zapmancer.core.common.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.smach.zapmancer.core.datastore.createDataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
@Configuration
@ComponentScan("com.smach.zapmancer.core")
class CoreModule {
    @Single
    fun provideDataStore(): DataStore<Preferences> {
        return createDataStore()
    }
}
