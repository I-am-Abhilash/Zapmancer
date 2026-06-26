package com.smach.zapmancer.core.network

actual object Environment {
    actual fun config(): EnvironmentConfig = EnvironmentConfig(
        baseUrl = "http://127.0.0.1:8090/",
    )
}