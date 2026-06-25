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
 * @param appDeclaration Optional additional module declarations to include
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {

        modules(
            coreModule,           // Core utilities and common dependencies first
            dataModule,          // Data layer repositories next  
            presentationModule,   // UI-related components last
            appModule(),         // Main application view models
        )
        
        appDeclaration()
    }
}

/**
 * Creates the main application module with all necessary dependencies.
 */
fun appModule(): Module = module {
    
    /**
     * Main ViewModel for managing overall app state and navigation.
     */
    viewModelOf(::MainViewModel)
    
    includes(
        coreModule,
        dataModule,
        presentationModule,
    )
}


