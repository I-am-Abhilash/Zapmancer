package com.smach.zapmancer.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.slf4j.LoggerFactory

/**
 * DatabaseFactory is responsible for setting up the connection pool (HikariCP),
 * running database migrations (Flyway), and connecting the Exposed ORM.
 */
object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(DatabaseFactory::class.java)
    private var dataSource: HikariDataSource? = null

    /**
     * Initializes the database connection and migrations.
     * @param config The database configuration (driver, url, user, password).
     */
    fun init(config: DatabaseConfig) {
        val hikariConfig = HikariConfig().apply {
            driverClassName = config.driver
            jdbcUrl = config.url
            username = config.user
            password = config.password
            maximumPoolSize = 20
            minimumIdle = 5
            idleTimeout = 300000
            maxLifetime = 1800000
            connectionTimeout = 10000
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val ds = HikariDataSource(hikariConfig)
        dataSource = ds

        val flyway = Flyway.configure().dataSource(ds).load()
        flyway.migrate()

        Database.connect(ds)
        logger.info("Database initialized and Flyway migrations applied successfully.")

        Runtime.getRuntime().addShutdownHook(
            Thread {
                close()
            },
        )
    }

    /**
     * Closes the HikariCP connection pool on application shutdown.
     */
    fun close() {
        try {
            dataSource?.close()
            logger.info("HikariCP DataSource closed successfully.")
        } catch (e: Exception) {
            logger.error("Error closing HikariCP DataSource: ${e.message}")
        }
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
