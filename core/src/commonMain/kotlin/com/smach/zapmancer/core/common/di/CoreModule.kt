package com.smach.zapmancer.core.common.di

import com.smach.zapmancer.core.common.utils.DataStoreStorage
import com.smach.zapmancer.core.common.utils.createDatabaseDriver
import com.smach.zapmancer.core.database.AppDatabase
import com.smach.zapmancer.core.monitoring.AnalyticsService
import com.smach.zapmancer.core.monitoring.NapierAnalyticsService
import com.smach.zapmancer.core.network.ktor.provideHttpClient
import com.smach.zapmancer.core.network.session.SessionManager
import org.koin.dsl.module

val coreModule =
    module {
        single { createDatabaseDriver() }
        single { AppDatabase(get()) }
        single { DataStoreStorage(get()) }

        single<AnalyticsService> { NapierAnalyticsService() }

        single { SessionManager(get()) }
        single { provideHttpClient(get()) }
    }
