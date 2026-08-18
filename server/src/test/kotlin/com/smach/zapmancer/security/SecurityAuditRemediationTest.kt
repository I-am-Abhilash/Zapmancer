package com.smach.zapmancer.security

import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.proposal.repository.ProposalsRepository
import com.smach.zapmancer.proposal.service.ProposalsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SecurityAuditRemediationTest {

    private val authRepository = mockk<AuthRepository>(relaxed = true)
    private val authService = AuthService(repository = authRepository)

    private val proposalsRepository = mockk<ProposalsRepository>(relaxed = true)
    private val proposalsService = ProposalsService(repository = proposalsRepository)

    @Test
    fun testVerifyOtpSuccess() {
        runBlocking {
            coEvery { authRepository.verifyAndConsumeOtp("user@zapmancer.com", "849201") } returns true

            val response = authService.verifyOtp("user@zapmancer.com", "849201")

            assertTrue(response.success)
            assertEquals("OTP verified successfully.", response.message)
        }
    }

    @Test
    fun testVerifyOtpInvalidCodeFails() {
        runBlocking {
            coEvery { authRepository.verifyAndConsumeOtp("user@zapmancer.com", "000000") } returns false

            val ex = assertFailsWith<ApiException> {
                authService.verifyOtp("user@zapmancer.com", "000000")
            }
            assertEquals(ErrorCode.BAD_REQUEST, ex.code)
        }
    }

    @Test
    fun testResetPasswordFlowSuccess() {
        runBlocking {
            coEvery { authRepository.verifyAndConsumeOtp("victim@zapmancer.com", "849201") } returns true
            coEvery { authRepository.updatePasswordByEmail("victim@zapmancer.com", any()) } returns true
            coEvery { authRepository.updatePassword("victim@zapmancer.com", any()) } returns true

            val response = authService.resetPassword("victim@zapmancer.com", "849201", "NewSecurePassword123!")

            assertTrue(response.success)
        }
    }

    @Test
    fun testResetPasswordShortPasswordFails() {
        runBlocking {
            val ex = assertFailsWith<ApiException> {
                authService.resetPassword("victim@zapmancer.com", "849201", "short")
            }
            assertEquals(ErrorCode.BAD_REQUEST, ex.code)
        }
    }

    @Test
    fun testResetPasswordInvalidOtpFails() {
        runBlocking {
            coEvery { authRepository.verifyAndConsumeOtp("victim@zapmancer.com", "000000") } returns false

            val ex = assertFailsWith<ApiException> {
                authService.resetPassword("victim@zapmancer.com", "000000", "NewSecurePassword123!")
            }
            assertEquals(ErrorCode.BAD_REQUEST, ex.code)
        }
    }

    @Test
    fun testProposalsBolaProtectionBlocksNonOwner() {
        runBlocking {
            // Project belongs to user_owner
            coEvery { proposalsRepository.getProjectOwnerId("proj_100") } returns "user_owner"

            // user_attacker tries to view proposals
            val ex = assertFailsWith<ApiException> {
                proposalsService.getProposalsForProject("user_attacker", "proj_100")
            }
            assertEquals(ErrorCode.FORBIDDEN, ex.code)
        }
    }

    @Test
    fun testProposalsAntiSelfBiddingBlocksOwner() {
        runBlocking {
            // Project belongs to user_owner
            coEvery { proposalsRepository.getProjectOwnerId("proj_100") } returns "user_owner"

            val req = SubmitProposalRequest(
                projectId = "proj_100",
                freelancerName = "Owner",
                freelancerRole = "Dev",
                pitchContent = "Self bid",
                budget = "$100",
                timelineDays = "5",
                projectType = "Fixed",
            )

            // user_owner tries to submit a proposal on their own project
            val ex = assertFailsWith<ApiException> {
                proposalsService.submitProposal("user_owner", req)
            }
            assertEquals(ErrorCode.FORBIDDEN, ex.code)
        }
    }
}
