package com.smach.zapmancer.app

import com.smach.zapmancer.auth.di.authModule
import com.smach.zapmancer.auth.routing.authRouting
import com.smach.zapmancer.database.DatabaseConfig
import com.smach.zapmancer.database.DatabaseFactory
import com.smach.zapmancer.framework.configureFramework
import com.smach.zapmancer.framework.di.storageModule
import com.smach.zapmancer.home.di.homeModule
import com.smach.zapmancer.home.routing.homeRouting
import com.smach.zapmancer.messages.di.messagesModule
import com.smach.zapmancer.messages.routing.messagesRouting
import com.smach.zapmancer.notifications.di.notificationsModule
import com.smach.zapmancer.notifications.routing.notificationsRouting
import com.smach.zapmancer.projects.di.projectsModule
import com.smach.zapmancer.projects.routing.projectsRouting
import com.smach.zapmancer.proposals.di.proposalsModule
import com.smach.zapmancer.proposals.routing.proposalsRouting
import com.smach.zapmancer.recommendations.createGorseModule
import com.smach.zapmancer.security.configureSecurity
import com.smach.zapmancer.settings.di.settingsModule
import com.smach.zapmancer.settings.routing.settingsRouting
import com.smach.zapmancer.users.di.usersModule
import com.smach.zapmancer.users.routing.usersRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    // 1. Database initialisation
    val dbConfig = DatabaseConfig(
        driver = environment.config.property("storage.driver").getString(),
        url = environment.config.property("storage.jdbcUrl").getString(),
        user = environment.config.property("storage.user").getString(),
        password = environment.config.property("storage.password").getString(),
    )
    DatabaseFactory.init(dbConfig)

    // 2. Gorse recommendation engine base URL
    val gorseBaseUrl = environment.config.propertyOrNull("gorse.baseUrl")?.getString()
        ?: "http://localhost:8088"

    // 3. Configure shared framework plugins (Koin DI, Serialization, CORS, Rate-limit, etc.)
    configureFramework(
        listOf(
            // Core infrastructure
            storageModule,
            createGorseModule(gorseBaseUrl),
            // Feature modules
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

    // 4. JWT Authentication
    configureSecurity()

    // 5. Register all feature routes
    routing {
        authRouting()
        usersRouting()
        homeRouting()
        projectsRouting()
        proposalsRouting()
        messagesRouting()
        notificationsRouting()
        settingsRouting()
    }
}
