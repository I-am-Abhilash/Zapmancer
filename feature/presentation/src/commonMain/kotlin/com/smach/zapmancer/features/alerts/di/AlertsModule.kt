package com.smach.zapmancer.features.alerts.di

import com.smach.zapmancer.features.alerts.viewmodel.NotificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val alertsModule = module {
    viewModelOf(::NotificationViewModel)
}
