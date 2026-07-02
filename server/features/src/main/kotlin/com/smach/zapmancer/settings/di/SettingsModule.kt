package com.smach.zapmancer.settings.di

import com.smach.zapmancer.settings.data.SettingsRepository
import com.smach.zapmancer.settings.domain.SettingsService
import org.koin.dsl.module

val settingsModule = module {
    single { SettingsRepository() }
    single { SettingsService(get()) }
}
