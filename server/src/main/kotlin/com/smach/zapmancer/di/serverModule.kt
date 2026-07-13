package com.smach.zapmancer.di

import com.smach.zapmancer.auth.data.AuthRepository
import com.smach.zapmancer.auth.domain.AuthService
import com.smach.zapmancer.home.data.HomeRepository
import com.smach.zapmancer.home.domain.HomeService
import com.smach.zapmancer.messages.data.MessageRepository
import com.smach.zapmancer.messages.domain.ConnectionManager
import com.smach.zapmancer.messages.domain.MessageService
import com.smach.zapmancer.notifications.data.NotificationsRepository
import com.smach.zapmancer.notifications.domain.NotificationsService
import com.smach.zapmancer.projects.data.ProjectsRepository
import com.smach.zapmancer.projects.domain.ProjectsService
import com.smach.zapmancer.proposal.data.ProposalsRepository
import com.smach.zapmancer.proposal.domain.ProposalsService
import com.smach.zapmancer.settings.data.SettingsRepository
import com.smach.zapmancer.settings.domain.SettingsService
import com.smach.zapmancer.users.data.UsersRepository
import com.smach.zapmancer.users.domain.UsersService
import com.smach.zapmancer.users.domain.UsersServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthRepository)
    singleOf(::AuthService)
}

val homeModule = module {
    singleOf(::HomeRepository)
    singleOf(::HomeService)
}

val messagesModule = module {
    singleOf(::MessageRepository)
    singleOf(::ConnectionManager)
    singleOf(::MessageService)
}

val notificationsModule = module {
    singleOf(::NotificationsRepository)
    singleOf(::NotificationsService)
}

val projectsModule = module {
    singleOf(::ProjectsRepository)
    singleOf(::ProjectsService)
}

val proposalsModule = module {
    singleOf(::ProposalsRepository)
    singleOf(::ProposalsService)
}

val settingsModule = module {
    singleOf(::SettingsRepository)
    singleOf(::SettingsService)
}

val usersModule = module {
    singleOf(::UsersRepository)
    singleOf(::UsersServiceImpl) { bind<UsersService>() }
}
