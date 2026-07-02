package com.smach.zapmancer.features.settings.di

import com.smach.zapmancer.features.settings.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
