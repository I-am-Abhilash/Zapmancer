package com.smach.zapmancer.api

import at.favre.lib.crypto.bcrypt.BCrypt
import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.auth.repository.AuthUserRecord
import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.core.common.dto.UpdateProjectRequest
import com.smach.zapmancer.core.security.JwtConfig
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.notifications.repository.NotificationsRepository
import com.smach.zapmancer.notifications.service.NotificationsService
import com.smach.zapmancer.projects.repository.ProjectsRepository
import com.smach.zapmancer.projects.service.ProjectsService
import com.smach.zapmancer.proposal.repository.ProposalRecord
import com.smach.zapmancer.proposal.repository.ProposalsRepository
import com.smach.zapmancer.proposal.service.ProposalsService
import com.smach.zapmancer.users.repository.UsersRepository
import com.smach.zapmancer.users.service.UsersServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ApiCompletenessLifecycleTest {

    @Test
    fun testJwtConfigRoleEmbeddingAndPrincipal() {
        val tokens = JwtConfig.generateTokens("usr_admin_1", "admin@zapmancer.com", "ADMIN")
        assertNotNull(tokens.accessToken)
        assertNotNull(tokens.refreshToken)

        val principal = UserPrincipal("usr_admin_1", "admin@zapmancer.com", "ADMIN")
        assertEquals("ADMIN", principal.role)
    }

    @Test
    fun testProjectsLifecycle() {
        runBlocking {
            val repo = mockk<ProjectsRepository>(relaxed = true)
            val service = ProjectsService(repo)

            // 1. Get my projects
            coEvery { repo.getMyProjects("client_123") } returns listOf(
                Project(
                    id = "proj_1",
                    category = "DESIGN",
                    title = "Landing Page Design",
                    postedTime = "1 hour ago",
                    location = "Remote",
                    isPaymentVerified = true,
                    budgetRange = "$1000 - $2000",
                    projectType = "Fixed Price",
                    skills = listOf("Figma", "UI/UX"),
                    isSaved = false,
                ),
            )

            val myProjects = service.getMyProjects("client_123")
            assertEquals(1, myProjects.size)
            assertEquals("Landing Page Design", myProjects[0].title)

            // 2. Update project
            coEvery { repo.update("proj_1", "client_123", any()) } returns true
            val updateRes = service.updateProject("client_123", "proj_1", UpdateProjectRequest(title = "Updated Title"))
            assertTrue(updateRes.success)

            // 3. Update project unauthorized
            coEvery { repo.update("proj_1", "other_client", any()) } returns false
            assertFailsWith<ApiException> {
                service.updateProject("other_client", "proj_1", UpdateProjectRequest(title = "Hacked"))
            }

            // 4. Delete project
            coEvery { repo.delete("proj_1", "client_123") } returns true
            val deleteRes = service.deleteProject("client_123", "proj_1")
            assertTrue(deleteRes.success)
        }
    }

    @Test
    fun testProposalsLifecycle() {
        runBlocking {
            val repo = mockk<ProposalsRepository>(relaxed = true)
            val service = ProposalsService(repo)

            // 1. Submit proposal
            coEvery { repo.getProjectOwnerId("proj_100") } returns "client_owner"
            coEvery { repo.submit("freelancer_1", any()) } returns 1
            val submitRes = service.submitProposal(
                "freelancer_1",
                SubmitProposalRequest(
                    projectId = "proj_100",
                    freelancerName = "Bob",
                    freelancerRole = "Android Dev",
                    pitchContent = "I can build this in 2 weeks",
                    budget = "$1500",
                    timelineDays = "14",
                ),
            )
            assertTrue(submitRes.success)

            // Cannot submit proposal to own project
            assertFailsWith<ApiException> {
                service.submitProposal(
                    "client_owner",
                    SubmitProposalRequest(
                        projectId = "proj_100",
                        freelancerName = "Owner",
                        freelancerRole = "Owner",
                        pitchContent = "Myself",
                        budget = "$100",
                        timelineDays = "1",
                    ),
                )
            }

            // 2. Get my proposals
            coEvery { repo.getByFreelancer("freelancer_1") } returns listOf(
                Proposal(
                    id = 1,
                    projectId = "proj_100",
                    freelancerId = "freelancer_1",
                    freelancerName = "Bob",
                    freelancerRole = "Android Dev",
                    pitchContent = "I can build this in 2 weeks",
                    budget = "$1500",
                    timelineDays = "14",
                    status = "PENDING",
                ),
            )
            val myProposals = service.getMyProposals("freelancer_1")
            assertEquals(1, myProposals.size)
            assertEquals("PENDING", myProposals[0].status)

            // 3. Accept proposal (Client owner)
            coEvery { repo.getProposalById(1) } returns ProposalRecord(
                id = 1,
                projectId = "proj_100",
                freelancerId = "freelancer_1",
                freelancerName = "Bob",
                freelancerRole = "Android Dev",
                pitchContent = "Pitch",
                budget = "$1500",
                timelineDays = "14",
                projectType = "Fixed Price",
                status = "PENDING",
                createdAt = "2026-08-18T10:00:00Z",
            )
            coEvery { repo.acceptProposalAndFormContract(1, "client_owner", "freelancer_1", "proj_100") } returns true
            val acceptRes = service.acceptProposal("client_owner", 1)
            assertTrue(acceptRes.success)

            // 4. Reject proposal (Client owner)
            coEvery { repo.updateStatus(1, "REJECTED") } returns true
            val rejectRes = service.rejectProposal("client_owner", 1)
            assertTrue(rejectRes.success)

            // 5. Withdraw proposal (Freelancer owner)
            coEvery { repo.withdraw(1, "freelancer_1") } returns true
            val withdrawRes = service.withdrawProposal("freelancer_1", 1)
            assertTrue(withdrawRes.success)

            // 6. Withdraw proposal unauthorized
            coEvery { repo.withdraw(1, "stranger") } returns false
            assertFailsWith<ApiException> {
                service.withdrawProposal("stranger", 1)
            }
        }
    }

    @Test
    fun testReviewsAndAccountDeactivation() {
        runBlocking {
            val repo = mockk<UsersRepository>(relaxed = true)
            val service = UsersServiceImpl(repo)

            coEvery { repo.freelancerExists("freelancer_target") } returns true
            coEvery { repo.createReview("client_author", "freelancer_target", any()) } returns Review(
                id = 10,
                authorId = "client_author",
                authorName = "Client Alice",
                authorRole = "Product Lead",
                content = "Exceptional delivery and communication!",
                rating = 5,
                authorAvatarUrl = null,
            )

            // 1. Submit review
            val review = service.createReview("client_author", "freelancer_target", CreateReviewRequest(content = "Great!", rating = 5))
            assertEquals(5, review.rating)
            assertEquals("Exceptional delivery and communication!", review.content)

            // Cannot review oneself
            assertFailsWith<ApiException> {
                service.createReview("freelancer_target", "freelancer_target", CreateReviewRequest(content = "I am awesome", rating = 5))
            }

            // 2. Get reviews
            coEvery { repo.getReviews("freelancer_target", 10, 0) } returns listOf(review)
            val reviews = service.getReviews("freelancer_target", 10, 0)
            assertEquals(1, reviews.size)

            // 3. Deactivate account
            coEvery { repo.deactivateAccount("user_leaving") } returns true
            val deactRes = service.deactivateAccount("user_leaving")
            assertTrue(deactRes.success)
        }
    }

    @Test
    fun testChangePasswordFlow() {
        runBlocking {
            val repo = mockk<AuthRepository>(relaxed = true)
            val service = AuthService(repo)

            val currentHash = BCrypt.withDefaults().hashToString(12, "old_password_123".toCharArray())
            coEvery { repo.findById("user_test") } returns AuthUserRecord(
                id = "user_test",
                username = "testuser",
                email = "test@example.com",
                passwordHash = currentHash,
                role = "FREELANCER",
            )
            coEvery { repo.updatePasswordById("user_test", any()) } returns true

            // 1. Successful change
            val res = service.changePassword("user_test", "old_password_123", "new_secure_password_456")
            assertTrue(res.success)

            // 2. Wrong current password fails
            assertFailsWith<ApiException> {
                service.changePassword("user_test", "wrong_old_password", "new_secure_password_456")
            }

            // 3. Too short new password fails
            assertFailsWith<ApiException> {
                service.changePassword("user_test", "old_password_123", "short")
            }
        }
    }

    @Test
    fun testNotificationReadStatus() {
        runBlocking {
            val repo = mockk<NotificationsRepository>(relaxed = true)
            val service = NotificationsService(repo)

            coEvery { repo.markAsRead(42, "user_1") } returns true
            val readOne = service.markAsRead(42, "user_1")
            assertTrue(readOne.success)

            coEvery { repo.markAllAsRead("user_1") } returns true
            val readAll = service.markAllAsRead("user_1")
            assertTrue(readAll.success)
        }
    }
}
