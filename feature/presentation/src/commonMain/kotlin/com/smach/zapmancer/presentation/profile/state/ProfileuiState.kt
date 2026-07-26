package com.smach.zapmancer.presentation.profile.state

import com.smach.zapmancer.domain.model.PortfolioItem
import com.smach.zapmancer.domain.model.ProfileReview
import com.smach.zapmancer.domain.model.UserProfile

data class ProfileUiState(
    val name: String = "",
    val role: String = "",
    val location: String = "",
    val ranking: String = "",
    val isTopRated: Boolean = false,
    val projectsCount: Int = 0,
    val rating: Double = 0.0,
    val experience: String = "",
    val about: String = "",
    val skills: List<String> = emptyList(),
    val portfolioItems: List<PortfolioItem> = emptyList(),
    val reviews: List<ProfileReview> = emptyList(),
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isHireSuccess: Boolean = false,
    val isOwnProfile: Boolean = true,
)

fun UserProfile.toUiState(isOwnProfile: Boolean): ProfileUiState = ProfileUiState(
    name = name,
    role = role,
    location = location.orEmpty(),
    ranking = ranking.orEmpty(),
    isTopRated = isTopRated,
    projectsCount = projectsCount,
    rating = rating,
    experience = experience.orEmpty(),
    about = about.orEmpty(),
    skills = skills,
    portfolioItems = portfolioItems,
    reviews = reviews,
    avatarUrl = avatarUrl.orEmpty(),
    isOwnProfile = isOwnProfile,
)
