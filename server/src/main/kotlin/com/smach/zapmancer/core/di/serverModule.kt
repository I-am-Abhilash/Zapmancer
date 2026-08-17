package com.smach.zapmancer.core.di

import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.home.repository.HomeRepository
import com.smach.zapmancer.home.service.HomeService
import com.smach.zapmancer.landingpage.service.LandingPageService
import com.smach.zapmancer.messages.repository.MessageRepository
import com.smach.zapmancer.messages.service.ConnectionManager
import com.smach.zapmancer.messages.service.MessageService
import com.smach.zapmancer.notifications.repository.NotificationsRepository
import com.smach.zapmancer.notifications.service.NotificationsService
import com.smach.zapmancer.projects.repository.ProjectsRepository
import com.smach.zapmancer.projects.service.ProjectsService
import com.smach.zapmancer.proposal.repository.ProposalsRepository
import com.smach.zapmancer.proposal.service.ProposalsService
import com.smach.zapmancer.settings.repository.SettingsRepository
import com.smach.zapmancer.settings.service.SettingsService
import com.smach.zapmancer.users.repository.UsersRepository
import com.smach.zapmancer.users.service.UsersService
import com.smach.zapmancer.users.service.UsersServiceImpl
import com.smach.zapmancer.messages.redis.RedisClientService
import com.smach.zapmancer.kyc.client.OpenBiometricsClient
import com.smach.zapmancer.kyc.repository.KycRepository
import com.smach.zapmancer.kyc.security.Ed25519ReceiptService
import com.smach.zapmancer.kyc.service.KycService
import io.ktor.server.application.Application
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.smach.zapmancer.core.verification.ResendEmailService
import com.smach.zapmancer.core.verification.TelnyxSmsService

val authModule = module {
    single {
        val config = get<Application>().environment.config
        val apiKey = config.propertyOrNull("resend.apiKey")?.getString()
        val fromEmail = config.propertyOrNull("resend.fromEmail")?.getString() ?: "Zapmancer <noreply@zapmancer.com>"
        ResendEmailService(apiKey, fromEmail)
    }
    single {
        val config = get<Application>().environment.config
        val apiKey = config.propertyOrNull("telnyx.apiKey")?.getString()
        val fromNumber = config.propertyOrNull("telnyx.fromNumber")?.getString() ?: "+18005550199"
        TelnyxSmsService(apiKey, fromNumber)
    }
    singleOf(::AuthRepository)
    single {
        AuthService(
            repository = get(),
            resendEmailService = get(),
            telnyxSmsService = get()
        )
    }
}


val homeModule = module {
    singleOf(::HomeRepository)
    singleOf(::HomeService)
}

val messagesModule = module {
    single {
        val config = get<Application>().environment.config
        val host = config.propertyOrNull("redis.host")?.getString() ?: "localhost"
        val port = config.propertyOrNull("redis.port")?.getString()?.toIntOrNull() ?: 6379
        val password = config.propertyOrNull("redis.password")?.getString()
        RedisClientService(host, port, password)
    }
    singleOf(::MessageRepository)
    single { ConnectionManager(redisClientService = get()) }
    single {
        val config = get<Application>().environment.config
        val bucketName = config.propertyOrNull("storage.bucket")?.getString() ?: "zapmancer-assets"
        MessageService(
            repository = get(),
            connectionManager = get(),
            storageService = getOrNull(),
            bucketName = bucketName
        )
    }
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

val landingPageModule = module {
    singleOf(::LandingPageService)
}

val kycModule = module {
    single {
        val config = get<Application>().environment.config
        val baseUrl = config.propertyOrNull("openbiometrics.baseUrl")?.getString() ?: "http://localhost:8000"
        val apiKey = config.propertyOrNull("openbiometrics.apiKey")?.getString()
        OpenBiometricsClient(baseUrl, apiKey)
    }
    single {
        val config = get<Application>().environment.config
        val privateKey = config.propertyOrNull("kyc.ed25519PrivateKey")?.getString()
        Ed25519ReceiptService(privateKey)
    }
    singleOf(::KycRepository)
    single {
        val config = get<Application>().environment.config
        val bucketName = config.propertyOrNull("storage.bucket")?.getString() ?: "zapmancer-assets"
        KycService(
            kycRepository = get(),
            openBiometricsClient = get(),
            receiptService = get(),
            storageService = get(),
            bucketName = bucketName
        )
    }
}

