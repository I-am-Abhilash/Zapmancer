package com.smach.zapmancer.core.recommendations

import org.koin.dsl.module

/**
 * Koin module factory for the Gorse Recommendation Engine integration.
 * @param baseUrl The base URL of the Gorse server (e.g., http://localhost:8088).
 */
fun createGorseModule(baseUrl: String) = module {
    single { GorseClient(baseUrl = baseUrl) }
}
