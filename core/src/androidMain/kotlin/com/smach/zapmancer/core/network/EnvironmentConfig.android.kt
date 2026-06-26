package com.smach.zapmancer.core.network

actual object Environment {
    actual fun config(): EnvironmentConfig = EnvironmentConfig(
        // Android emulator routes 10.0.2.2 to the host machine's loopback interface,
        // so the dev backend running on the host is reachable as expected.
        baseUrl = "http://10.0.2.2:8090/",
    )
}