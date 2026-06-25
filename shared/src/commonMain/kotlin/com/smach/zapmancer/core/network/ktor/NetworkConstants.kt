package com.smach.zapmancer.core.network.ktor

import android.content.Context
import androidx.datastore.preferences.DataStorePreferences
import org.koin.android.ext.koin.inject

object NetworkConstants {
    private val context: Context by inject()
    
    const val BASE_URL_DEV = "http://127.0.0.1:8090/"
    const val BASE_URL_STAGING = "https://staging.zapmancer.com/api"
    const val BASE_URL_PRODUCTION = "https://api.zapmancer.com/v1"

    fun getBaseUrl(): String {
        return when (context.applicationInfo.versionCode) {
            in 0 until 1_000 -> BASE_URL_DEV // Development builds
            else if (BuildConfig.DEBUG || BuildConfig.BUILD_TYPE == "dev") -> BASE_URL_DEV
            else -> BASE_URL_PRODUCTION // Production builds default to production URL
        }
    }

    /**
     * Get the appropriate base URL based on build configuration.
     * Override this method in a custom implementation for environment-specific URLs.
     */
    fun getBaseUrlForEnvironment(environment: String): String = when (environment) {
        "dev" -> BASE_URL_DEV
        "staging" -> BASE_URL_STAGING
        else -> BASE_URL_PRODUCTION
    }

    /**
     * Get the API version for request headers.
     */
    const val API_VERSION_HEADER = "v1"
}
