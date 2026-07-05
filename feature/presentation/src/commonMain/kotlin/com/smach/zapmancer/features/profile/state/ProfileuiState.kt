package com.smach.zapmancer.features.profile.state

import com.smach.zapmancer.domain.model.PortfolioItem
import com.smach.zapmancer.domain.model.ProfileReview

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
