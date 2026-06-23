package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val role: String,
    val location: String,
    val ranking: String,
    val isTopRated: Boolean,
    val projectsCount: Int,
    val rating: Double,
    val experience: String,
    val about: String,
    val skills: List<String>,
    val portfolioItems: List<PortfolioItem>,
    val reviews: List<ProfileReview>,
    val avatarUrl: String? = null,
)

@Serializable
data class PortfolioItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
)

@Serializable
data class ProfileReview(
    val authorId: String,
    val id: String,
    val authorName: String,
    val authorRole: String,
    val content: String,
    val rating: Int,
    val authorAvatarUrl: String? = null
)




