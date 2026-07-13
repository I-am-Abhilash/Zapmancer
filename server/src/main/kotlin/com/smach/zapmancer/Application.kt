package com.smach.zapmancer

import com.smach.zapmancer.auth.routing.authRouting
import com.smach.zapmancer.database.DatabaseConfig
import com.smach.zapmancer.database.DatabaseFactory
import com.smach.zapmancer.di.authModule
import com.smach.zapmancer.di.homeModule
import com.smach.zapmancer.di.messagesModule
import com.smach.zapmancer.di.notificationsModule
import com.smach.zapmancer.di.projectsModule
import com.smach.zapmancer.di.proposalsModule
import com.smach.zapmancer.di.settingsModule
import com.smach.zapmancer.di.usersModule
import com.smach.zapmancer.framework.configureFramework
import com.smach.zapmancer.framework.di.storageModule
import com.smach.zapmancer.home.routing.homeRouting
import com.smach.zapmancer.messages.routing.messageRouting
import com.smach.zapmancer.notifications.routing.notificationsRouting
import com.smach.zapmancer.projects.routing.projectsRouting
import com.smach.zapmancer.proposal.routing.proposalsRouting
import com.smach.zapmancer.recommendations.createGorseModule
import com.smach.zapmancer.security.configureSecurity
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

    val gorseBaseUrl = environment.config.propertyOrNull("gorse.baseUrl")?.getString()
        ?: "http://localhost:8088"

    configureFramework(
        listOf(
            storageModule,
            createGorseModule(gorseBaseUrl),
            authModule,
            usersModule,
            homeModule,
            projectsModule,
            proposalsModule,
            messagesModule,
            notificationsModule,
            settingsModule,
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
    }
}
