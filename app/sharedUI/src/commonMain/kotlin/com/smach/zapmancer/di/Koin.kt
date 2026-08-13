package com.smach.zapmancer.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin


@KoinApplication
object ZapmancerApp

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin<ZapmancerApp> {
    appDeclaration()
}

@Module
@Configuration
@ComponentScan("com.smach.zapmancer")
class AppModule
