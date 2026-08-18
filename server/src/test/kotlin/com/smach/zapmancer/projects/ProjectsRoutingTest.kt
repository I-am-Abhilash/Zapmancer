package com.smach.zapmancer.projects

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.core.common.dto.UpdateProjectRequest
import com.smach.zapmancer.projects.repository.ProjectsRepository
import com.smach.zapmancer.projects.service.ProjectsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ProjectsRoutingTest {

    @Test
    fun testGetProjectsAndRecommendations() = runBlocking {
        val repo = mockk<ProjectsRepository>()
        val service = ProjectsService(repo)

        val project = Project(
            id = "proj_kmp",
            category = "Development",
            title = "Build KMP App",
            postedTime = "2h ago",
            location = "Remote",
            isPaymentVerified = true,
            budgetRange = "$3000-$5000",
            projectType = "Fixed Price",
            skills = listOf("Kotlin", "Compose"),
            isSaved = false,
            status = "OPEN"
        )

        coEvery { repo.getAllProjects("usr_1", any(), any(), any(), any(), any()) } returns listOf(project)
        coEvery { repo.getRecommendedProjects("usr_1", 10) } returns listOf(project)

        val projects = service.getProjects("usr_1")
        assertEquals(1, projects.size)
        assertEquals("Build KMP App", projects[0].title)

        val recommended = service.getRecommendedProjects("usr_1", 10)
        assertEquals(1, recommended.size)
        assertEquals("OPEN", recommended[0].status)
    }

    @Test
    fun testProjectValidationAndCreation() = runBlocking {
        val repo = mockk<ProjectsRepository>()
        val service = ProjectsService(repo)

        // Blank title fails
        assertFailsWith<ApiException> {
            service.createProject(
                "client_1",
                CreateProjectRequest(category = "Dev", title = "", budgetRange = "$1000")
            )
        }

        // Blank budget fails
        assertFailsWith<ApiException> {
            service.createProject(
                "client_1",
                CreateProjectRequest(category = "Dev", title = "New App", budgetRange = "")
            )
        }

        coEvery { repo.create("client_1", any()) } returns "proj_created"
        val res = service.createProject(
            "client_1",
            CreateProjectRequest(category = "Development", title = "New Kotlin App", budgetRange = "$1000-$2000")
        )
        assertTrue(res.success)
    }

    @Test
    fun testProjectStatusTransition() = runBlocking {
        val repo = mockk<ProjectsRepository>()
        val service = ProjectsService(repo)

        coEvery { repo.updateStatus("proj_1", "client_1", "IN_PROGRESS") } returns true
        val res = service.updateProjectStatus("client_1", "proj_1", "IN_PROGRESS")
        assertTrue(res.success)
        assertEquals("Project status transitioned to IN_PROGRESS.", res.message)

        // Invalid status fails
        assertFailsWith<ApiException> {
            service.updateProjectStatus("client_1", "proj_1", "INVALID_STATUS")
        }

        // Unauthorized update fails
        coEvery { repo.updateStatus("proj_1", "attacker", "CLOSED") } returns false
        assertFailsWith<ApiException> {
            service.updateProjectStatus("attacker", "proj_1", "CLOSED")
        }
    }

    @Test
    fun testSaveAndApplyToProject() = runBlocking {
        val repo = mockk<ProjectsRepository>()
        val service = ProjectsService(repo)

        coEvery { repo.toggleSave("usr_1", "proj_1", true) } returns true
        val saveRes = service.saveProject("usr_1", "proj_1", true)
        assertTrue(saveRes.success)

        val detail = ProjectDetail(
            id = "proj_1",
            category = "Dev",
            title = "Test",
            postedTime = "now",
            location = "Remote",
            isPaymentVerified = true,
            projectScope = null,
            deliverables = emptyList(),
            skills = emptyList(),
            budgetRange = "$100",
            projectType = "Fixed",
            timeline = null,
            estStart = null,
            clientName = "Client",
            clientIndustry = "Tech",
            clientLocation = "USA",
            clientProjectsCount = 1,
            clientRating = 5.0,
            isSaved = false,
            isClientActive = true,
            isIdentityVerified = true,
            isPhoneVerified = true,
            status = "OPEN"
        )
        coEvery { repo.findById("proj_1", "usr_1") } returns detail
        coEvery { repo.apply("usr_1", "proj_1") } returns true

        val applyRes = service.applyToProject("usr_1", "proj_1")
        assertTrue(applyRes.success)
    }
}
