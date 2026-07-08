package com.smach.zapmancer.home.di

import com.smach.zapmancer.home.data.HomeRepository
import com.smach.zapmancer.home.domain.HomeService
import org.koin.dsl.module

val homeModule = module {
    single { HomeRepository() }
    single { HomeService(get()) }
}
