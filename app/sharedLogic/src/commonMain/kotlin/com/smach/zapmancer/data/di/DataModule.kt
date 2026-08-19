package com.smach.zapmancer.data.di

import com.smach.zapmancer.core.network.ktor.ktorClient
import com.smach.zapmancer.core.network.session.SessionManager
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("com.smach.zapmancer.data")
class DataModule {
    @Single
    fun provideHttpClient(sessionManager: SessionManager): HttpClient = ktorClient(sessionManager)
}
