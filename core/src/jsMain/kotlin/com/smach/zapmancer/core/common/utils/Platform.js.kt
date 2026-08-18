package com.smach.zapmancer.core.common.utils

class JsPlatform : Platform {
    override val name: String = "Web / JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual val isWebPlatform: Boolean = true
