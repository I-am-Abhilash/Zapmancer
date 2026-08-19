package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.LandingPageDto
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.FeaturedProject
import com.smach.zapmancer.domain.model.FeaturedTalent
import com.smach.zapmancer.domain.model.HeroData
import com.smach.zapmancer.domain.model.HowItWorksStep
import com.smach.zapmancer.domain.model.LandingPageData
import com.smach.zapmancer.domain.model.MarketplaceCategory
import com.smach.zapmancer.domain.model.ProductCard
import com.smach.zapmancer.domain.model.ResourceCard
import com.smach.zapmancer.domain.model.SolutionCard
import com.smach.zapmancer.domain.model.SuccessStory
import com.smach.zapmancer.domain.model.TrustSafetyItem
import com.smach.zapmancer.domain.model.TwoPathsData
import com.smach.zapmancer.domain.repository.LandingPageRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single(binds = [LandingPageRepository::class])
class LandingPageRepositoryImpl(
    private val client: HttpClient,
) : LandingPageRepository {

    override suspend fun getLandingPageData(): Result<LandingPageData, DataError.Network> = safeApiCall<LandingPageDto> {
        client.get("landing-page/data")
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> Result.Success(getFallbackLandingPageData())
        }
    }
}

private fun LandingPageDto.toDomain(): LandingPageData = LandingPageData(
    hero = HeroData(
        headline = hero.headline,
        subtext = hero.subtext,
        primaryCtaText = hero.primaryCtaText,
        secondaryCtaText = hero.secondaryCtaText,
        popularRoles = hero.popularRoles,
    ),
    twoPaths = TwoPathsData(
        sectionTitle = twoPaths.sectionTitle,
        hiringTitle = twoPaths.hiringTitle,
        hiringDescription = twoPaths.hiringDescription,
        hiringCta = twoPaths.hiringCta,
        freelancingTitle = twoPaths.freelancingTitle,
        freelancingDescription = twoPaths.freelancingDescription,
        freelancingCta = twoPaths.freelancingCta,
    ),
    products = products.map { ProductCard(it.id, it.title, it.description, it.ctaText, it.iconName) },
    solutions = solutions.map { SolutionCard(it.id, it.category, it.title, it.description, it.ctaText) },
    howItWorksSteps = howItWorksSteps.map { HowItWorksStep(it.stepNumber, it.title, it.description) },
    marketplaceCategories = marketplaceCategories.map { MarketplaceCategory(it.categoryName, it.popularSkills) },
    featuredTalent = featuredTalent.map { FeaturedTalent(it.id, it.name, it.role, it.rating, it.hourlyRate, it.avatarUrl, it.topSkills) },
    featuredProjects = featuredProjects.map { FeaturedProject(it.id, it.title, it.categories, it.budgetRange, it.postedTime) },
    trustItems = trustItems.map { TrustSafetyItem(it.id, it.title, it.description) },
    successStories = successStories.map { SuccessStory(it.id, it.title, it.subtitle, it.storySnippet, it.imageUrl) },
    resourceCards = resourceCards.map { ResourceCard(it.id, it.targetAudience, it.title, it.description, it.ctaText) },
)

fun getFallbackLandingPageData(): LandingPageData = LandingPageData(
    hero = HeroData(
        headline = "Great work starts with the right people.",
        subtext = "Zapmancer connects ambitious businesses with talented independent professionals to turn ideas into real work — from a single project to an entire team.",
        primaryCtaText = "Find Talent",
        secondaryCtaText = "Find Work",
        popularRoles = listOf("Developers", "Designers", "Writers", "Marketers", "Creators", "Specialists"),
    ),
    twoPaths = TwoPathsData(
        sectionTitle = "Whatever you're building, start here.",
        hiringTitle = "Find the people who can make it happen.",
        hiringDescription = "Find skilled freelancers, specialists, and teams to bring your ideas to life.",
        hiringCta = "Find Talent →",
        freelancingTitle = "Find work that moves you forward.",
        freelancingDescription = "Discover projects that match your skills, build your reputation, and grow your career.",
        freelancingCta = "Find Work →",
    ),
    products = listOf(
        ProductCard("p1", "Find Talent", "Discover skilled professionals for your next project.", "Explore →", "Search"),
        ProductCard("p2", "Post a Project", "Share your goals and connect with people who can bring them to life.", "Post Now →", "Work"),
        ProductCard("p3", "Find Work", "Explore opportunities that match your skills and ambitions.", "Browse →", "Work"),
        ProductCard("p4", "Build a Team", "Assemble the right combination of specialists for complex projects.", "Build →", "Group"),
        ProductCard("p5", "Workspace", "Keep your projects, conversations, files, and collaboration in one place.", "Open →", "Devices"),
        ProductCard("p6", "Payments", "Manage project payments and milestone protection with confidence.", "Learn More →", "Payments"),
    ),
    solutions = listOf(
        SolutionCard("s1", "Startups", "Build your vision without building a massive team.", "Find developers, designers, and marketers for your MVP.", "Explore Startup Solutions →"),
        SolutionCard("s2", "Small Businesses", "Get the expertise you need, when you need it.", "From websites and branding to marketing and software development.", "Explore Small Business Solutions →"),
        SolutionCard("s3", "Agencies", "Scale your capabilities without scaling your overhead.", "Bring in specialized talent when your team needs additional capacity.", "Explore Agency Solutions →"),
        SolutionCard("s4", "Enterprises", "Access specialized talent for complex work.", "Build flexible teams and connect with professionals across disciplines.", "Explore Enterprise Solutions →"),
    ),
    howItWorksSteps = listOf(
        HowItWorksStep("01", "Tell us what you need", "Describe your project, goals, timeline, and skills you're looking for."),
        HowItWorksStep("02", "Find the right people", "Discover freelancers and professionals who match your requirements."),
        HowItWorksStep("03", "Collaborate", "Work together, communicate, share files, and keep your project moving."),
        HowItWorksStep("04", "Get it done", "Complete your project, build your reputation, and move on to what's next."),
    ),
    marketplaceCategories = listOf(
        MarketplaceCategory("Development", listOf("Android", "iOS", "Kotlin", "Backend", "Web", "AI/ML")),
        MarketplaceCategory("Design", listOf("UI/UX Design", "Brand Identity", "Graphic Design", "Motion Design", "3D Design")),
        MarketplaceCategory("Marketing", listOf("SEO", "Content Marketing", "Social Media", "Paid Ads", "Growth Hacking")),
        MarketplaceCategory("Writing & Content", listOf("Copywriting", "Technical Writing", "Blogs", "Scriptwriting", "Editing")),
        MarketplaceCategory("AI & Data", listOf("Prompt Engineering", "Data Science", "Python", "Computer Vision", "LLMs")),
    ),
    featuredTalent = listOf(
        FeaturedTalent("t1", "Alex Rivera", "Android & KMP Developer", 4.9, "$45/hr", "", listOf("Kotlin", "Compose", "KMP")),
        FeaturedTalent("t2", "Sarah Chen", "UI/UX & Product Designer", 5.0, "$60/hr", "", listOf("Figma", "Design Systems", "UX")),
        FeaturedTalent("t3", "Julian Vancore", "Fullstack & Cloud Engineer", 4.8, "$55/hr", "", listOf("Node.js", "Ktor", "AWS")),
    ),
    featuredProjects = listOf(
        FeaturedProject("fp1", "Build Kotlin Multiplatform App", listOf("Android", "iOS", "Backend"), "$2,000–$4,000", "Posted 2h ago"),
        FeaturedProject("fp2", "Design System & Web App UI", listOf("UI/UX", "Figma", "Design"), "$1,500–$3,000", "Posted 4h ago"),
        FeaturedProject("fp3", "AI Customer Agent API Integration", listOf("AI", "Python", "Ktor"), "$3,000–$5,000", "Posted 1d ago"),
    ),
    trustItems = listOf(
        TrustSafetyItem("tr1", "Verified Profiles", "Identity and skills verified by automated tests and proof of work."),
        TrustSafetyItem("tr2", "Milestone Payments", "Funds held securely in escrow until milestones are reviewed and approved."),
        TrustSafetyItem("tr3", "Transparent Reviews", "Honest client and freelancer ratings backed by verified transactions."),
        TrustSafetyItem("tr4", "Dispute Protection", "Dedicated support team ensuring fair project resolution."),
    ),
    successStories = listOf(
        SuccessStory("ss1", "From Idea to MVP in 3 Weeks", "Startup Story", "A stealth AI startup hired 2 engineers on Zapmancer and launched their beta in record time.", ""),
        SuccessStory("ss2", "Turning Freelance Skills into a Career", "Freelancer Spotlight", "Alex scaled his independent mobile app studio to 6-figure revenue using Zapmancer.", ""),
        SuccessStory("ss3", "Scaling Agency Capacity on Demand", "Agency Growth", "BrightMedia expanded its client delivery capability 3x without hiring full-time staff.", ""),
    ),
    resourceCards = listOf(
        ResourceCard("r1", "For Clients", "Learn how to hire better", "Guides, checklists, and advice for managing freelance talent.", "Explore Client Guides →"),
        ResourceCard("r2", "For Freelancers", "Build a career on your skills", "Learn how to find clients, write winning proposals, and grow reputation.", "Explore Freelancer Guides →"),
        ResourceCard("r3", "Community", "Zapmancer Builder Network", "Stories, discussions, and insights from people working independently.", "Join Community →"),
    ),
)
