package com.smach.zapmancer.core.common.di

import com.smach.zapmancer.core.common.utils.DataStoreStorage
import com.smach.zapmancer.core.common.utils.createDataStore
import com.smach.zapmancer.core.database.db.AppDatabase
import com.smach.zapmancer.core.database.db.getDatabaseBuilder
import com.smach.zapmancer.core.database.db.getRoomDatabase
import com.smach.zapmancer.core.monitoring.AnalyticsService
import com.smach.zapmancer.core.monitoring.NapierAnalyticsService
import com.smach.zapmancer.core.network.ktor.provideHttpClient
import com.smach.zapmancer.core.network.session.SessionManager
import org.koin.dsl.module

val coreModule =
    module {
        single { createDataStore() }
        single { DataStoreStorage(get()) }

        single {
            getDatabaseBuilder()
        }
        single<AppDatabase> {
            getRoomDatabase(get())
        }
        single { get<AppDatabase>().articleDao() }
        single { get<AppDatabase>().recentSearchDao() }

        single<AnalyticsService> { NapierAnalyticsService() }

        single { SessionManager(get()) }
        single { provideHttpClient(get()) }
    }
