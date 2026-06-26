package com.smach.zapmancer.di

import com.smach.zapmancer.MainViewModel
import com.smach.zapmancer.core.common.di.coreModule
import com.smach.zapmancer.data.di.dataModule
import com.smach.zapmancer.features.common.di.presentationModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Initializes Koin dependency injection container for the application.
 *
 * Note: [appModule] already includes [coreModule], [dataModule], and [presentationModule]
 * via `includes(...)`, so passing them again at the top-level would double-register bindings.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        modules(appModule())
        appDeclaration()
    }
}

/**
 * Creates the main application module. Sub-modules are included here (not at the
 * top-level) so each is registered exactly once.
 */
fun appModule(): Module = module {
    viewModelOf(::MainViewModel)

    includes(
        coreModule,
        dataModule,
        presentationModule,
    )
}


