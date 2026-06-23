package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isNewUser: Boolean = false,
)
