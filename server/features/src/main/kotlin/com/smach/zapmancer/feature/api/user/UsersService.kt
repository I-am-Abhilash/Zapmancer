package com.smach.zapmancer.feature.api.user

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.users.api.UpdateProfileRequest
import com.smach.zapmancer.users.api.UserProfile

/**
 * Public contract for Profile-related operations.
 * Focused on public profile display and hire flow (not auth).
 */
interface UsersService {
    /** Retrieve own profile (authenticated user). */
    suspend fun getOwnProfile(userId: String): DomainResult<UserProfile>

    /** Retrieve any user's public profile. */
    suspend fun getPublicProfile(userId: String): DomainResult<UserProfile>

    /** Update authenticated user's own profile. */
    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): DomainResult<UserProfile>

    /** Dispatch a hire notification to a freelancer. */
    suspend fun hireFreelancer(clientId: String, freelancerId: String): DomainResult<CommonResponse>
}
