package com.smach.zapmancer.data.repository

import kotlinx.serialization.Serializable

@Serializable
internal data class CommonResponse(
    val message: String? = null,
)
