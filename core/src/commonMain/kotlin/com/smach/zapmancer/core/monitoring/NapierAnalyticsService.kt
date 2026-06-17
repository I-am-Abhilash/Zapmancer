package com.smach.zapmancer.core.monitoring

import io.github.aakira.napier.Napier

class NapierAnalyticsService : AnalyticsService {
    override fun logEvent(
        name: String,
        params: Map<String, String>,
    ) {
        Napier.d("Analytics Event: $name, Params: $params")
    }

    override fun logScreen(screenName: String) {
        Napier.d("Analytics Screen: $screenName")
    }

    override fun logError(
        throwable: Throwable,
        message: String?,
    ) {
        Napier.e(message ?: throwable.message ?: "Unknown Error", throwable)
    }
}
