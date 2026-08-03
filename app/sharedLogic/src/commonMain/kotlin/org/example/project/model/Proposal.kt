package com.smach.zapmancer.domain.model

data class Proposal(
    val id: String = "",
    val projectId: String = "",
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String,
)
