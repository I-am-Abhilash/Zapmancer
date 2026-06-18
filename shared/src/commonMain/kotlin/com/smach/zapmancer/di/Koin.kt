package com.smach.zapmancer.di

//import com.smach.zapmancer.MainViewModel
//import com.smach.zapmancer.data.di.dataModule
import com.smach.zapmancer.core.common.di.coreModule
import com.smach.zapmancer.features.common.di.presentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule())
}

fun appModule() = module {
//    viewModelOf(::MainViewModel)
    includes(
        coreModule,
//        dataModule,
        presentationModule,
    )
}
