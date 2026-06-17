package com.smach.zapmancer.data.di

import com.smach.zapmancer.data.repository.ArticleRepositoryImpl
import com.smach.zapmancer.data.repository.AuthRepositoryImpl
import com.smach.zapmancer.data.repository.ProfileRepositoryImpl
import com.smach.zapmancer.data.repository.SearchRepositoryImpl
import com.smach.zapmancer.domain.repository.ArticleRepository
import com.smach.zapmancer.domain.repository.AuthRepository
import com.smach.zapmancer.domain.repository.ProfileRepository
import com.smach.zapmancer.domain.repository.SearchRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule =
    module {
        factoryOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }
        factoryOf(::ArticleRepositoryImpl) { bind<ArticleRepository>() }
        factoryOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
        factoryOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
    }
