package com.smach.zapmancer.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

/**
 * DatabaseFactory is responsible for setting up the connection pool (HikariCP),
 * running database migrations (Flyway), and connecting the Exposed ORM.
 */
object DatabaseFactory {

    /**
     * Initializes the database connection and migrations.
     * @param config The database configuration (driver, url, user, password).
     */
    fun init(config: DatabaseConfig) {
        val dataSource = HikariDataSource(
            HikariConfig().apply {
                driverClassName = config.driver
                jdbcUrl = config.url
                username = config.user
                password = config.password
                maximumPoolSize = 3 // Limited for local/dev, increase for production
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            },
        )

        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()

        Database.connect(dataSource)
    }

    /**
     * Helper function to wrap database queries in a suspended transaction.
     * Use this in your repositories/services to perform DB operations safely.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T = suspendTransaction { block() }
}

/**
 * Simple data class to hold database credentials.
 */
data class DatabaseConfig(
    val driver: String,
    val url: String,
    val user: String,
    val password: String,
)
