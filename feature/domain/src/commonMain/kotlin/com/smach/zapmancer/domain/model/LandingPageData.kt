package com.smach.zapmancer.domain.model

data class LandingPageData(
    val hero: HeroData,
    val twoPaths: TwoPathsData,
    val products: List<ProductCard>,
    val solutions: List<SolutionCard>,
    val howItWorksSteps: List<HowItWorksStep>,
    val marketplaceCategories: List<MarketplaceCategory>,
    val featuredTalent: List<FeaturedTalent>,
    val featuredProjects: List<FeaturedProject>,
    val trustItems: List<TrustSafetyItem>,
    val successStories: List<SuccessStory>,
    val resourceCards: List<ResourceCard>,
)

data class HeroData(
    val headline: String,
    val subtext: String,
    val primaryCtaText: String,
    val secondaryCtaText: String,
    val popularRoles: List<String>,
)

data class TwoPathsData(
    val sectionTitle: String,
    val hiringTitle: String,
    val hiringDescription: String,
    val hiringCta: String,
    val freelancingTitle: String,
    val freelancingDescription: String,
    val freelancingCta: String,
)

data class ProductCard(
    val id: String,
    val title: String,
    val description: String,
    val ctaText: String,
    val iconName: String,
)

data class SolutionCard(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val ctaText: String,
)

data class HowItWorksStep(
    val stepNumber: String,
    val title: String,
    val description: String,
)

data class MarketplaceCategory(
    val categoryName: String,
    val popularSkills: List<String>,
)

data class FeaturedTalent(
    val id: String,
    val name: String,
    val role: String,
    val rating: Double,
    val hourlyRate: String,
    val avatarUrl: String,
    val topSkills: List<String>,
)

data class FeaturedProject(
    val id: String,
    val title: String,
    val categories: List<String>,
    val budgetRange: String,
    val postedTime: String,
)

data class TrustSafetyItem(
    val id: String,
    val title: String,
    val description: String,
)

data class SuccessStory(
    val id: String,
    val title: String,
    val subtitle: String,
    val storySnippet: String,
    val imageUrl: String,
)

data class ResourceCard(
    val id: String,
    val targetAudience: String,
    val title: String,
    val description: String,
    val ctaText: String,
)
