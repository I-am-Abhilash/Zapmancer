package com.smach.zapmancer.core.common.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val isWebPlatform: Boolean

