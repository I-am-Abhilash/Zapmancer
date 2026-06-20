package com.smach.zapmancer.data.repository

import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository

class ProjectRepositoryImpl : ProjectRepository {
    
    private val projectsList = mutableListOf(
        Project(
            id = 1,
            category = "Development",
            status = "ACTIVE",
            title = "Neural Engine Alpha",
            description = "High-performance inference engine optimization cycle using WebAssembly and custom shaders.",
            progress = 78,
            tags = listOf("WASM", "WebGL", "Rust"),
            membersCount = 2
        ),
        Project(
            id = 2,
            category = "Design",
            status = "PENDING",
            title = "Lumina Design System",
            description = "Unified token-based architecture for multi-platform interfaces across iOS, Android, and Web.",
            showImagePlaceholder = true,
            tags = listOf("Tokens", "Figma", "Design Ops"),
            footerText = "Review: Oct 24"
        )
    )

    private val projectDetailsMap = mutableMapOf(
        "1" to ProjectDetail(
            id = "1",
            category = "Development",
            title = "Neural Engine Alpha",
            postedTime = "2 hours ago",
            location = "Remote (Global)",
            isPaymentVerified = true,
            projectScope = "We are seeking a senior frontend engineer to architect and build the core components of our new real-time Neural Engine Alpha monitoring dashboard. The project requires extreme performance optimization, handling up to 10,000 data updates per second with zero UI lag.",
            deliverables = listOf(
                "Low-latency Canvas-based charting engine.",
                "Real-time order book visualization component.",
                "Websocket management layer with robust reconnection logic.",
                "Custom theme engine for high-contrast trading environments."
            ),
            skills = listOf("TypeScript", "React.js", "WebAssembly", "WebSocket API", "Canvas/WebGL", "Rust", "FinTech Experience"),
            budgetRange = "$12,000 - $18,000",
            projectType = "Fixed Price Project",
            timeline = "6-8 Weeks",
            estStart = "Oct 2024",
            clientName = "QuantStream Labs",
            clientIndustry = "Financial Tech",
            clientLocation = "London, UK",
            clientProjectsCount = 42,
            clientRating = 4.9,
            isSaved = false,
            isClientActive = true,
            isIdentityVerified = true,
            isPhoneVerified = true
        ),
        "2" to ProjectDetail(
            id = "2",
            category = "Design",
            title = "Lumina Design System",
            postedTime = "1 day ago",
            location = "Remote",
            isPaymentVerified = true,
            projectScope = "Seeking a UI/UX architect to refine and implement the core token architecture of our Lumina Design System. You will coordinate with both engineering and product design teams.",
            deliverables = listOf(
                "Design tokens JSON specification.",
                "Figma component library updates.",
                "Documentation site structure."
            ),
            skills = listOf("Figma", "Design Tokens", "UI/UX", "Design Systems"),
            budgetRange = "$5,000 - $8,000",
            projectType = "Fixed Price Project",
            timeline = "3-4 Weeks",
            estStart = "Oct 2024",
            clientName = "Lumina Tech",
            clientIndustry = "SaaS",
            clientLocation = "San Francisco, US",
            clientProjectsCount = 12,
            clientRating = 4.7,
            isSaved = false,
            isClientActive = true,
            isIdentityVerified = true,
            isPhoneVerified = false
        )
    )

    override suspend fun getProjects(): List<Project> {
        return projectsList
    }

    override suspend fun getProjectDetail(id: String): ProjectDetail {
        return projectDetailsMap[id] ?: throw Exception("Project $id not found")
    }

    override suspend fun saveProject(id: String, isSaved: Boolean) {
        val detail = projectDetailsMap[id] ?: throw Exception("Project $id not found")
        projectDetailsMap[id] = detail.copy(isSaved = isSaved)
    }

    override suspend fun applyForProject(id: String) {
        // Mock apply action
    }
}
