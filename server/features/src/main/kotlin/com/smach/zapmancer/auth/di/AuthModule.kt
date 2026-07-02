package com.smach.zapmancer.auth.di

import com.smach.zapmancer.auth.data.AuthRepository
import com.smach.zapmancer.auth.domain.AuthService
import org.koin.dsl.module

val authModule = module {
    single { AuthRepository() }
    single { AuthService(get()) }
}
