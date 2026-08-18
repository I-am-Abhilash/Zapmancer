package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val category: String,
    val title: String,
    val postedTime: String,
    val location: String,
    val isPaymentVerified: Boolean,
    val budgetRange: String,
    val projectType: String,
    val skills: List<String>,
    val isSaved: Boolean,
    val status: String = "OPEN",
)

@Serializable
data class ProjectDetail(
    val id: String,
    val category: String,
    val title: String,
    val postedTime: String,
    val location: String,
    val isPaymentVerified: Boolean,
    val projectScope: String?,
    val deliverables: List<String>,
    val skills: List<String>,
    val budgetRange: String,
    val projectType: String,
    val timeline: String?,
    val estStart: String?,
    val clientName: String,
    val clientIndustry: String,
    val clientLocation: String,
    val clientProjectsCount: Int,
    val clientRating: Double,
    val isSaved: Boolean,
    val isClientActive: Boolean,
    val isIdentityVerified: Boolean,
    val isPhoneVerified: Boolean,
    val status: String = "OPEN",
)

@Serializable
data class SaveProjectRequest(val isSaved: Boolean)

@Serializable
data class CreateProjectRequest(
    val category: String,
    val title: String,
    val location: String = "Remote",
    val budgetRange: String,
    val projectType: String = "Fixed Price",
    val projectScope: String? = null,
    val deliverables: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val timeline: String? = null,
    val estStart: String? = null,
)

@Serializable
data class UpdateProjectRequest(
    val category: String? = null,
    val title: String? = null,
    val location: String? = null,
    val budgetRange: String? = null,
    val projectType: String? = null,
    val projectScope: String? = null,
    val deliverables: List<String>? = null,
    val skills: List<String>? = null,
    val timeline: String? = null,
    val estStart: String? = null,
)

@Serializable
data class UpdateProjectStatusRequest(
    val status: String,
)
