package com.smach.zapmancer.landingpage

import com.smach.zapmancer.landingpage.service.LandingPageService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LandingPageRoutingTest {

    @Test
    fun testLandingPagePayload() {
        val service = LandingPageService()
        val data = service.getLandingPageData()

        assertNotNull(data.hero)
        assertEquals("Great work starts with the right people.", data.hero.headline)
        assertTrue(data.hero.popularRoles.isNotEmpty())

        assertEquals(6, data.products.size)
        assertEquals(4, data.solutions.size)
        assertEquals(4, data.howItWorksSteps.size)
        assertEquals(5, data.marketplaceCategories.size)
        assertEquals(3, data.featuredTalent.size)
        assertEquals(3, data.featuredProjects.size)
        assertEquals(4, data.trustItems.size)
        assertEquals(3, data.successStories.size)
        assertEquals(3, data.resourceCards.size)
    }
}
