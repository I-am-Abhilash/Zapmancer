package com.smach.zapmancer.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String,
)

@Serializable
data class RefreshTokenResponse(
    val id: String,
    val accessToken: String,
    val refreshToken: String,
)
