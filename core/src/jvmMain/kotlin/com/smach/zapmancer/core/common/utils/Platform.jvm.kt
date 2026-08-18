package com.smach.zapmancer.core.common.utils

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val isWebPlatform: Boolean = false
