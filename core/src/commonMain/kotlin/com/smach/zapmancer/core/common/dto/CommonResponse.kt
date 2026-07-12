package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val success: Boolean,
    val message: String,
)
