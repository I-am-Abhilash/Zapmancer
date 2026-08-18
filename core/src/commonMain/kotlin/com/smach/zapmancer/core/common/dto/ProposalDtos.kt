package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class Proposal(
    val id: Int = 0,
    val projectId: String = "",
    val freelancerId: String = "",
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String = "Fixed Price",
    val status: String = "PENDING",
    val createdAt: String = "",
)

@Serializable
data class SubmitProposalRequest(
    val projectId: String,
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String = "Fixed Price",
)
