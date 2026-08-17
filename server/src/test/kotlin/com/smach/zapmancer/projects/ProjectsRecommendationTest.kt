package com.smach.zapmancer.projects

import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.projects.repository.ProjectsRepository
import com.smach.zapmancer.projects.service.ProjectsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectsRecommendationTest {

    private val repository = mockk<ProjectsRepository>(relaxed = true)
    private val service = ProjectsService(repository = repository)

    @Test
    fun testGetRecommendedProjectsReturnsMatchingList() = runBlocking {
        val mockProjects = listOf(
            Project(
                id = "proj_1",
                category = "Mobile Development",
                title = "Kotlin Multiplatform App",
                postedTime = "2h ago",
                location = "Remote",
                isPaymentVerified = true,
                budgetRange = "$2000 - $4000",
                projectType = "Fixed",
                skills = listOf("Kotlin", "Compose Multiplatform", "Ktor"),
                isSaved = false
            ),
            Project(
                id = "proj_2",
                category = "Backend Development",
                title = "Ktor Redis Microservice",
                postedTime = "5h ago",
                location = "Remote",
                isPaymentVerified = true,
                budgetRange = "$1500 - $3000",
                projectType = "Fixed",
                skills = listOf("Kotlin", "PostgreSQL", "Redis"),
                isSaved = true
            )
        )

        coEvery { repository.getRecommendedProjects("user_123", 10) } returns mockProjects

        val result = service.getRecommendedProjects("user_123", 10)

        assertEquals(2, result.size)
        assertEquals("proj_1", result[0].id)
        assertTrue(result[0].skills.contains("Kotlin"))
    }
}
