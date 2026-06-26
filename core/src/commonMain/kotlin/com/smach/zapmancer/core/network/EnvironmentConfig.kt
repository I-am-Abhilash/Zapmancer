package com.smach.zapmancer.core.network

/**
 * Per-platform environment configuration. The actual [baseUrl] is supplied by the
 * platform-specific source set via [Environment].
 */
data class EnvironmentConfig(
    val baseUrl: String,
)

/**
 * Platform-specific environment provider. Each platform (Android, iOS, Web)
 * supplies its own `actual` implementation of [config].
 *
 * - Android emulator: `http://10.0.2.2:8090/` (loopback to host)
 * - iOS / Web: `http://127.0.0.1:8090/` (localhost)
 *
 * Production deployments should layer a build-config or runtime override on top
 * of this seam.
 */
expect object Environment {
    fun config(): EnvironmentConfig
}