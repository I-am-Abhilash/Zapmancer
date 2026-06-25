package com.smach.zapmancer.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import app.cash.sqldelight.db.SqlDriver
import com.google.firebase.ktx.FirebaseAnalyticsLogger
import com.smach.zapmancer.MainViewModel
import com.smach.zapmancer.core.common.di.coreModule
import com.smach.zapmancer.core.database.AppDatabase
import com.smach.zapmancer.data.di.dataModule
import com.smach.zapmancer.features.alerts.screen.NotificationScreen
import com.smach.zapmancer.features.auth.state.LoginUiState
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
import com.smach.zapmancer.features.messages.viewmodel.MessagesDetailViewModel
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectsViewModel
import com.smach.zapmancer.features.proposal.viewmodel.ProposalsViewModel
import org.koin.core.context.startKoin
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
        // Android context initialization
        androidContext()
        
        // Application modules with proper ordering
        modules(
            coreModule,           // Core utilities and common dependencies first
            dataModule,          // Data layer repositories next  
            presentationModule,   // UI-related components last
            appModule(),         // Main application view models
            analyticsLoggerModule()  // Firebase Analytics integration if needed
        )
        
        // Additional custom modules can be added here as needed
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
    
    // Include core modules in proper dependency order
    includes(
        coreModule,           // Core utilities (networking, data store builders, etc.)
        dataModule,          // Data repositories and use cases
        presentationModule,   // UI components and view models
    )
}

/**
 * Analytics logger module for Firebase integration.
 */
fun analyticsLoggerModule(): Module = module {
    single<FirebaseAnalyticsLogger> { 
        object : FirebaseAnalyticsLogger {} 
    }
}

// Keep the original appModule function but mark as deprecated in favor of new structure
@Deprecated("Use initKoin() instead", ReplaceWith("initKoin"))
fun oldAppModule(): Module = module {
    viewModelOf(::MainViewModel)
    includes(
        coreModule,
        dataModule,
        presentationModule,
    )
}

/**
 * Creates a DataStore instance for preferences management.
 */
private val datastore: DataStore<Preferences> by lazy {
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { 
            // Use app-specific directory with unique name to avoid conflicts
            context.filesDir.resolve("datastore.preferences_pb") 
        },
        migrate = null,  // Add migration logic if needed in future versions
        preferencesName = "app_preferences"
    )
}

/**
 * Creates a database driver instance for SQL operations.
 */
private val sqlDriver: SqlDriver by lazy {
    AndroidDatabaseDriverBuilder().create()
}
