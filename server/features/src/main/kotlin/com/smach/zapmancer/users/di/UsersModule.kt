package com.smach.zapmancer.users.di

import com.smach.zapmancer.feature.api.user.UsersService
import com.smach.zapmancer.users.data.UsersRepository
import com.smach.zapmancer.users.domain.UsersServiceImpl
import org.koin.dsl.module

val usersModule = module {
    single { UsersRepository() }
    single<UsersService> { UsersServiceImpl(get()) }
}
