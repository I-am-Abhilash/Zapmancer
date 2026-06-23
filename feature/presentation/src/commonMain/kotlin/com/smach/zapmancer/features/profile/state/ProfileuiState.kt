package com.smach.zapmancer.features.profile.state

import com.smach.zapmancer.domain.model.PortfolioItem
import com.smach.zapmancer.domain.model.ProfileReview

data class ProfileUiState(
    val name: String = "Julian Vancore",
    val role: String = "Senior Systems Architect & Interaction Designer",
    val location: String = "San Francisco",
    val ranking: String = "2nd Runner Up",
    val isTopRated: Boolean = true,
    val projectsCount: Int = 124,
    val rating: Double = 9.8,
    val experience: String = "12yr",
    val about: String = "With over 12 years of experience in building mission-critical software architectures, I specialize in bridging the gap between high-density data systems and intuitive user interfaces. My approach is rooted in systematic precision and functional minimalism.",
    val skills: List<String> = listOf(
        "Systems Architecture", "TypeScript", "Rust", "Product Strategy",
        "UI/UX Engineering", "GraphQL", "Cloud Native"
    ),
    val portfolioItems: List<PortfolioItem> = emptyList(),
    val reviews: List<ProfileReview> = emptyList(),
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuBrBrKqoM8axW5MPKsBTP5b-rY47j3sPFMPKxLb9MC-OiKc2nVehBkyjSvjrG61iLhnECENazpIX7ZGYdSvJhKpIGWBgn-fNWKLOFOAoJvAOS7uUgeFV7IEUSxjbQHtWEbwQGrVnBP5GX0LOssfjYZWHQOHZeoQNPaT0aZZAB44DcV0MaETyz8F_dFWst5O4bhj6tODWrstc0H0BKuGeulwq4Nbqlg5_5SCdjeZWbq0lUi7AAm8ZezuoaO1rWJpKniR5CNjmrAo9eo",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isHireSuccess: Boolean = false,
    val isOwnProfile: Boolean = true
)
