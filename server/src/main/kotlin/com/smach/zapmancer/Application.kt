package com.smach.zapmancer

import com.smach.zapmancer.auth.routing.authRouting
import com.smach.zapmancer.core.database.DatabaseConfig
import com.smach.zapmancer.core.database.DatabaseFactory
import com.smach.zapmancer.core.di.authModule
import com.smach.zapmancer.core.di.homeModule
import com.smach.zapmancer.core.di.messagesModule
import com.smach.zapmancer.core.di.notificationsModule
import com.smach.zapmancer.core.di.projectsModule
import com.smach.zapmancer.core.di.proposalsModule
import com.smach.zapmancer.core.di.settingsModule
import com.smach.zapmancer.core.di.usersModule
import com.smach.zapmancer.core.framework.configureFramework
import com.smach.zapmancer.core.framework.di.storageModule
import com.smach.zapmancer.core.security.configureSecurity
import com.smach.zapmancer.core.di.landingPageModule
import com.smach.zapmancer.core.di.kycModule
import com.smach.zapmancer.home.routing.homeRouting
import com.smach.zapmancer.kyc.routing.kycRouting
import com.smach.zapmancer.landingpage.routing.landingPageRouting
import com.smach.zapmancer.messages.routing.messageRouting
import com.smach.zapmancer.notifications.routing.notificationsRouting
import com.smach.zapmancer.projects.routing.projectsRouting
import com.smach.zapmancer.proposal.routing.proposalsRouting
import com.smach.zapmancer.settings.routing.settingsRouting
import com.smach.zapmancer.users.routing.usersRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val dbConfig = DatabaseConfig(
        driver = environment.config.property("storage.driver").getString(),
        url = environment.config.property("storage.jdbcUrl").getString(),
        user = environment.config.property("storage.user").getString(),
        password = environment.config.property("storage.password").getString(),
    )
    DatabaseFactory.init(dbConfig)

    configureFramework(
        listOf(
            storageModule,
            authModule,
            usersModule,
            homeModule,
            projectsModule,
            proposalsModule,
            messagesModule,
            notificationsModule,
            settingsModule,
            landingPageModule,
            kycModule,
        ),
    )

    configureSecurity()

    routing {
        authRouting()
        usersRouting()
        homeRouting()
        projectsRouting()
        proposalsRouting()
        messageRouting()
        notificationsRouting()
        settingsRouting()
        landingPageRouting()
        kycRouting()
    }
}

