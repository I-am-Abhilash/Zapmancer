package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class LandingPageDto(
    val hero: HeroDataDto,
    val twoPaths: TwoPathsDataDto,
    val products: List<ProductCardDto>,
    val solutions: List<SolutionCardDto>,
    val howItWorksSteps: List<HowItWorksStepDto>,
    val marketplaceCategories: List<MarketplaceCategoryDto>,
    val featuredTalent: List<FeaturedTalentDto>,
    val featuredProjects: List<FeaturedProjectDto>,
    val trustItems: List<TrustSafetyItemDto>,
    val successStories: List<SuccessStoryDto>,
    val resourceCards: List<ResourceCardDto>,
)

@Serializable
data class HeroDataDto(
    val headline: String,
    val subtext: String,
    val primaryCtaText: String,
    val secondaryCtaText: String,
    val popularRoles: List<String>,
)

@Serializable
data class TwoPathsDataDto(
    val sectionTitle: String,
    val hiringTitle: String,
    val hiringDescription: String,
    val hiringCta: String,
    val freelancingTitle: String,
    val freelancingDescription: String,
    val freelancingCta: String,
)

@Serializable
data class ProductCardDto(
    val id: String,
    val title: String,
    val description: String,
    val ctaText: String,
    val iconName: String,
)

@Serializable
data class SolutionCardDto(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val ctaText: String,
)

@Serializable
data class HowItWorksStepDto(
    val stepNumber: String,
    val title: String,
    val description: String,
)

@Serializable
data class MarketplaceCategoryDto(
    val categoryName: String,
    val popularSkills: List<String>,
)

@Serializable
data class FeaturedTalentDto(
    val id: String,
    val name: String,
    val role: String,
    val rating: Double,
    val hourlyRate: String,
    val avatarUrl: String,
    val topSkills: List<String>,
)

@Serializable
data class FeaturedProjectDto(
    val id: String,
    val title: String,
    val categories: List<String>,
    val budgetRange: String,
    val postedTime: String,
)

@Serializable
data class TrustSafetyItemDto(
    val id: String,
    val title: String,
    val description: String,
)

@Serializable
data class SuccessStoryDto(
    val id: String,
    val title: String,
    val subtitle: String,
    val storySnippet: String,
    val imageUrl: String,
)

@Serializable
data class ResourceCardDto(
    val id: String,
    val targetAudience: String,
    val title: String,
    val description: String,
    val ctaText: String,
)
