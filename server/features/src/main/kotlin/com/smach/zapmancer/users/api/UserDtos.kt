package com.smach.zapmancer.users.api

import kotlinx.serialization.Serializable

// ---------------------------------------------------------------------------
// Public Profile DTOs
// ---------------------------------------------------------------------------

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val role: String,
    val location: String?,
    val ranking: String?,
    val isTopRated: Boolean,
    val projectsCount: Int,
    val rating: Double,
    val experience: String?,
    val about: String?,
    val skills: List<String>,
    val portfolioItems: List<PortfolioItem>,
    val reviews: List<Review>,
    val avatarUrl: String?,
)

@Serializable
data class PortfolioItem(
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String?,
)

@Serializable
data class Review(
    val id: Int,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val rating: Int,
    val authorAvatarUrl: String?,
)

// ---------------------------------------------------------------------------
// Requests
// ---------------------------------------------------------------------------

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val roleTitle: String? = null,
    val location: String? = null,
    val about: String? = null,
    val experience: String? = null,
    val skills: List<String>? = null,
    val avatarUrl: String? = null,
)
