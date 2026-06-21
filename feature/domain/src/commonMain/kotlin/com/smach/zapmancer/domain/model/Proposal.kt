package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Proposal(
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String
)
