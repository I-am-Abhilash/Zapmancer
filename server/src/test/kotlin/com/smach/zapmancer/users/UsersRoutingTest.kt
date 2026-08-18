package com.smach.zapmancer.users

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.users.repository.UsersRepository
import com.smach.zapmancer.users.service.UsersServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UsersRoutingTest {

    @Test
    fun testGetProfileAndPublicProfile() {
        runBlocking {
            val repo = mockk<UsersRepository>()
            val service = UsersServiceImpl(repo)

            val profile = UserProfile(
                id = "usr_1",
                name = "Alice Developer",
                role = "Senior Kotlin Engineer",
                location = "London, UK",
                ranking = "#1",
                isTopRated = true,
                projectsCount = 15,
                rating = 4.9,
                experience = "8 years",
                about = "Kotlin enthusiast",
                skills = listOf("Kotlin", "Ktor", "Compose"),
                portfolioItems = emptyList(),
                reviews = emptyList(),
                avatarUrl = null,
            )

            coEvery { repo.findProfile("usr_1") } returns profile

            val ownProfile = service.getOwnProfile("usr_1")
            assertEquals("Alice Developer", ownProfile.name)
            assertEquals(4.9, ownProfile.rating)

            val publicProfile = service.getPublicProfile("usr_1")
            assertEquals("Senior Kotlin Engineer", publicProfile.role)
        }
    }

    @Test
    fun testUpdateProfile() {
        runBlocking {
            val repo = mockk<UsersRepository>()
            val service = UsersServiceImpl(repo)

            val updatedProfile = UserProfile(
                id = "usr_1",
                name = "Alice Updated",
                role = "Staff Architect",
                location = "London, UK",
                ranking = "#1",
                isTopRated = true,
                projectsCount = 15,
                rating = 4.9,
                experience = "9 years",
                about = "Kotlin and Distributed Systems",
                skills = listOf("Kotlin", "Ktor", "Distributed Systems"),
                portfolioItems = emptyList(),
                reviews = emptyList(),
                avatarUrl = null,
            )

            coEvery { repo.updateProfile("usr_1", any()) } returns true
            coEvery { repo.findProfile("usr_1") } returns updatedProfile

            val result = service.updateProfile("usr_1", UpdateProfileRequest(name = "Alice Updated", roleTitle = "Staff Architect"))
            assertEquals("Alice Updated", result.name)
            assertEquals("Staff Architect", result.role)
        }
    }

    @Test
    fun testReviewSubmissionValidationAndSuccess() {
        runBlocking {
            val repo = mockk<UsersRepository>()
            val service = UsersServiceImpl(repo)

            coEvery { repo.freelancerExists("freelancer_99") } returns true
            coEvery { repo.createReview("client_10", "freelancer_99", any()) } returns Review(
                id = 5,
                authorId = "client_10",
                authorName = "Client Bob",
                authorRole = "Product Director",
                content = "Great speed and clean code!",
                rating = 5,
                authorAvatarUrl = null,
            )

            // Invalid rating (0 or >5)
            assertFailsWith<ApiException> {
                service.createReview("client_10", "freelancer_99", CreateReviewRequest(content = "Okay", rating = 6))
            }

            // Blank content
            assertFailsWith<ApiException> {
                service.createReview("client_10", "freelancer_99", CreateReviewRequest(content = "   ", rating = 4))
            }

            // Self-review prevention
            assertFailsWith<ApiException> {
                service.createReview("freelancer_99", "freelancer_99", CreateReviewRequest(content = "I am great", rating = 5))
            }

            // Successful submission
            val review = service.createReview("client_10", "freelancer_99", CreateReviewRequest(content = "Great speed and clean code!", rating = 5))
            assertEquals(5, review.rating)
            assertEquals("Great speed and clean code!", review.content)
        }
    }

    @Test
    fun testAccountDeactivation() {
        runBlocking {
            val repo = mockk<UsersRepository>()
            val service = UsersServiceImpl(repo)

            coEvery { repo.deactivateAccount("usr_deact") } returns true
            val response = service.deactivateAccount("usr_deact")
            assertTrue(response.success)
            assertEquals("Account deactivated successfully.", response.message)
        }
    }
}
