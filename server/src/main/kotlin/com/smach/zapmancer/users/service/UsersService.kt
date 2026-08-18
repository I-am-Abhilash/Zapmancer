package com.smach.zapmancer.users.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import org.koin.core.annotation.Single

/**
 * Public contract for Profile-related operations.
 * Focused on public profile display, reviews, hire flow, and account management.
 */
@Single
interface UsersService {
    /** Retrieve own profile (authenticated user). */
    suspend fun getOwnProfile(userId: String): UserProfile

    /** Retrieve any user's public profile. */
    suspend fun getPublicProfile(userId: String): UserProfile

    /** Update authenticated user's own profile. */
    suspend fun updateProfile(
        userId: String,
        request: UpdateProfileRequest,
    ): UserProfile

    /** Dispatch a hire notification to a freelancer. */
    suspend fun hireFreelancer(clientId: String, freelancerId: String): CommonResponse

    /** Submit a review for a user. */
    suspend fun createReview(authorId: String, subjectId: String, request: CreateReviewRequest): Review

    /** Retrieve paginated reviews for a user. */
    suspend fun getReviews(subjectId: String, limit: Int = 20, offset: Long = 0): List<Review>

    /** Deactivate/soft-delete own account. */
    suspend fun deactivateAccount(userId: String): CommonResponse
}
