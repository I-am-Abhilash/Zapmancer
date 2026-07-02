package com.smach.zapmancer.proposals.api

import kotlinx.serialization.Serializable

@Serializable
data class Proposal(
    val id: Int = 0,
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String = "Fixed Price",
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
