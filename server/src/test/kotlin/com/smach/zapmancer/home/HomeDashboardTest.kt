package com.smach.zapmancer.home

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.RecentActivity
import com.smach.zapmancer.home.repository.HomeRepository
import com.smach.zapmancer.home.repository.UserContext
import com.smach.zapmancer.home.service.HomeService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HomeDashboardTest {

    @Test
    fun testGetDashboard() = runBlocking {
        val repo = mockk<HomeRepository>()
        val service = HomeService(repo)

        coEvery { repo.getUserContext("usr_1") } returns UserContext(name = "David", isClientMode = true)
        coEvery { repo.getActiveProjectsCount("usr_1") } returns 3
        coEvery { repo.getCapacity("usr_1") } returns "85%"
        coEvery { repo.getRecentActivities("usr_1") } returns listOf(
            RecentActivity(
                id = "act_1",
                projectName = "Landing Page",
                category = "Design",
                categoryTag = "UI",
                status = "COMPLETED",
                date = "2026-08-18",
                value = "$1200"
            )
        )

        val dashboard = service.getDashboard("usr_1")
        assertEquals("David", dashboard.userName)
        assertEquals(3, dashboard.activeProjectsCount)
        assertEquals("85%", dashboard.totalCapacity)
        assertEquals(1, dashboard.recentActivities.size)
        assertTrue(dashboard.isClientMode)

        coEvery { repo.getUserContext("unknown") } returns null
        assertFailsWith<ApiException> {
            service.getDashboard("unknown")
        }
    }

    @Test
    fun testExportActivities() = runBlocking {
        val repo = mockk<HomeRepository>()
        val service = HomeService(repo)

        val activities = listOf(
            RecentActivity(
                id = "act_1",
                projectName = "Zapmancer KMP",
                category = "Development",
                categoryTag = "Kotlin",
                status = "IN_PROGRESS",
                date = "2026-08-18",
                value = "$2500"
            )
        )

        val res = service.exportActivities(activities)
        assertTrue(res.filePath.startsWith("exports/activities_"))
        val file = File(res.filePath)
        assertTrue(file.exists())
        val content = file.readText()
        assertTrue(content.contains("Zapmancer KMP"))
        file.delete()
    }
}
