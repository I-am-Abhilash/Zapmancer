package com.smach.zapmancer.domain.model

data class User(
    val id: String,
    val email: String,
    val isNewUser: Boolean = false,
)
