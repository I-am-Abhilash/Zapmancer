package com.smach.zapmancer.messages.di

import com.smach.zapmancer.messages.data.MessagesRepository
import com.smach.zapmancer.messages.domain.MessagesService
import org.koin.dsl.module

val messagesModule = module {
    single { MessagesRepository() }
    single { MessagesService(get()) }
}
