package com.smach.zapmancer.notifications.di

import com.smach.zapmancer.notifications.data.NotificationsRepository
import com.smach.zapmancer.notifications.domain.NotificationsService
import org.koin.dsl.module

val notificationsModule = module {
    single { NotificationsRepository() }
    single { NotificationsService(get()) }
}
