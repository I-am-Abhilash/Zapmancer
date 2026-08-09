package com.smach.zapmancer.landingpage.service

import com.smach.zapmancer.core.common.dto.FeaturedProjectDto
import com.smach.zapmancer.core.common.dto.FeaturedTalentDto
import com.smach.zapmancer.core.common.dto.HeroDataDto
import com.smach.zapmancer.core.common.dto.HowItWorksStepDto
import com.smach.zapmancer.core.common.dto.LandingPageDto
import com.smach.zapmancer.core.common.dto.MarketplaceCategoryDto
import com.smach.zapmancer.core.common.dto.ProductCardDto
import com.smach.zapmancer.core.common.dto.ResourceCardDto
import com.smach.zapmancer.core.common.dto.SolutionCardDto
import com.smach.zapmancer.core.common.dto.SuccessStoryDto
import com.smach.zapmancer.core.common.dto.TrustSafetyItemDto
import com.smach.zapmancer.core.common.dto.TwoPathsDataDto
import org.koin.core.annotation.Single

@Single
class LandingPageService {
    fun getLandingPageData(): LandingPageDto {
        return LandingPageDto(
            hero = HeroDataDto(
                headline = "Great work starts with the right people.",
                subtext = "Zapmancer connects ambitious businesses with talented independent professionals to turn ideas into real work — from a single project to an entire team.",
                primaryCtaText = "Find Talent",
                secondaryCtaText = "Find Work",
                popularRoles = listOf("Developers", "Designers", "Writers", "Marketers", "Creators", "Specialists"),
            ),
            twoPaths = TwoPathsDataDto(
                sectionTitle = "Whatever you're building, start here.",
                hiringTitle = "Find the people who can make it happen.",
                hiringDescription = "Find skilled freelancers, specialists, and teams to bring your ideas to life.",
                hiringCta = "Find Talent →",
                freelancingTitle = "Find work that moves you forward.",
                freelancingDescription = "Discover projects that match your skills, build your reputation, and grow your career.",
                freelancingCta = "Find Work →",
            ),
            products = listOf(
                ProductCardDto("p1", "Find Talent", "Discover skilled professionals for your next project.", "Explore →", "Search"),
                ProductCardDto("p2", "Post a Project", "Share your goals and connect with people who can bring them to life.", "Post Now →", "Work"),
                ProductCardDto("p3", "Find Work", "Explore opportunities that match your skills and ambitions.", "Browse →", "Work"),
                ProductCardDto("p4", "Build a Team", "Assemble the right combination of specialists for complex projects.", "Build →", "Group"),
                ProductCardDto("p5", "Workspace", "Keep your projects, conversations, files, and collaboration in one place.", "Open →", "Devices"),
                ProductCardDto("p6", "Payments", "Manage project payments and milestone protection with confidence.", "Learn More →", "Payments"),
            ),
            solutions = listOf(
                SolutionCardDto("s1", "Startups", "Build your vision without building a massive team.", "Find developers, designers, and marketers for your MVP.", "Explore Startup Solutions →"),
                SolutionCardDto("s2", "Small Businesses", "Get the expertise you need, when you need it.", "From websites and branding to marketing and software development.", "Explore Small Business Solutions →"),
                SolutionCardDto("s3", "Agencies", "Scale your capabilities without scaling your overhead.", "Bring in specialized talent when your team needs additional capacity.", "Explore Agency Solutions →"),
                SolutionCardDto("s4", "Enterprises", "Access specialized talent for complex work.", "Build flexible teams and connect with professionals across disciplines.", "Explore Enterprise Solutions →"),
            ),
            howItWorksSteps = listOf(
                HowItWorksStepDto("01", "Tell us what you need", "Describe your project, goals, timeline, and skills you're looking for."),
                HowItWorksStepDto("02", "Find the right people", "Discover freelancers and professionals who match your requirements."),
                HowItWorksStepDto("03", "Collaborate", "Work together, communicate, share files, and keep your project moving."),
                HowItWorksStepDto("04", "Get it done", "Complete your project, build your reputation, and move on to what's next."),
            ),
            marketplaceCategories = listOf(
                MarketplaceCategoryDto("Development", listOf("Android", "iOS", "Kotlin", "Backend", "Web", "AI/ML")),
                MarketplaceCategoryDto("Design", listOf("UI/UX Design", "Brand Identity", "Graphic Design", "Motion Design", "3D Design")),
                MarketplaceCategoryDto("Marketing", listOf("SEO", "Content Marketing", "Social Media", "Paid Ads", "Growth Hacking")),
                MarketplaceCategoryDto("Writing & Content", listOf("Copywriting", "Technical Writing", "Blogs", "Scriptwriting", "Editing")),
                MarketplaceCategoryDto("AI & Data", listOf("Prompt Engineering", "Data Science", "Python", "Computer Vision", "LLMs")),
            ),
            featuredTalent = listOf(
                FeaturedTalentDto("t1", "Alex Rivera", "Android & KMP Developer", 4.9, "$45/hr", "", listOf("Kotlin", "Compose", "KMP")),
                FeaturedTalentDto("t2", "Sarah Chen", "UI/UX & Product Designer", 5.0, "$60/hr", "", listOf("Figma", "Design Systems", "UX")),
                FeaturedTalentDto("t3", "Julian Vancore", "Fullstack & Cloud Engineer", 4.8, "$55/hr", "", listOf("Node.js", "Ktor", "AWS")),
            ),
            featuredProjects = listOf(
                FeaturedProjectDto("fp1", "Build Kotlin Multiplatform App", listOf("Android", "iOS", "Backend"), "$2,000–$4,000", "Posted 2h ago"),
                FeaturedProjectDto("fp2", "Design System & Web App UI", listOf("UI/UX", "Figma", "Design"), "$1,500–$3,000", "Posted 4h ago"),
                FeaturedProjectDto("fp3", "AI Customer Agent API Integration", listOf("AI", "Python", "Ktor"), "$3,000–$5,000", "Posted 1d ago"),
            ),
            trustItems = listOf(
                TrustSafetyItemDto("tr1", "Verified Profiles", "Identity and skills verified by automated tests and proof of work."),
                TrustSafetyItemDto("tr2", "Milestone Payments", "Funds held securely in escrow until milestones are reviewed and approved."),
                TrustSafetyItemDto("tr3", "Transparent Reviews", "Honest client and freelancer ratings backed by verified transactions."),
                TrustSafetyItemDto("tr4", "Dispute Protection", "Dedicated support team ensuring fair project resolution."),
            ),
            successStories = listOf(
                SuccessStoryDto("ss1", "From Idea to MVP in 3 Weeks", "Startup Story", "A stealth AI startup hired 2 engineers on Zapmancer and launched their beta in record time.", ""),
                SuccessStoryDto("ss2", "Turning Freelance Skills into a Career", "Freelancer Spotlight", "Alex scaled his independent mobile app studio to 6-figure revenue using Zapmancer.", ""),
                SuccessStoryDto("ss3", "Scaling Agency Capacity on Demand", "Agency Growth", "BrightMedia expanded its client delivery capability 3x without hiring full-time staff.", ""),
            ),
            resourceCards = listOf(
                ResourceCardDto("r1", "For Clients", "Learn how to hire better", "Guides, checklists, and advice for managing freelance talent.", "Explore Client Guides →"),
                ResourceCardDto("r2", "For Freelancers", "Build a career on your skills", "Learn how to find clients, write winning proposals, and grow reputation.", "Explore Freelancer Guides →"),
                ResourceCardDto("r3", "Community", "Zapmancer Builder Network", "Stories, discussions, and insights from people working independently.", "Join Community →"),
            ),
        )
    }
}

