package com.smach.zapmancer.core.monitoring

interface AnalyticsService {
    fun logEvent(
        name: String,
        params: Map<String, String> = emptyMap(),
    )

    fun logScreen(screenName: String)

    fun logError(
        throwable: Throwable,
        message: String? = null,
    )
}
