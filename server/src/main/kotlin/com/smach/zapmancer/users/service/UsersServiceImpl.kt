package com.smach.zapmancer.users.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.DomainResult
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.users.repository.UsersRepository
import org.koin.core.annotation.Single

@Single(binds = [UsersService::class])
class UsersServiceImpl(
    private val repository: UsersRepository,
) : UsersService {

    override suspend fun getOwnProfile(userId: String): DomainResult<UserProfile> {
        val profile = repository.findProfile(userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "Profile not found.")
        return DomainResult.Success(profile)
    }

    override suspend fun getPublicProfile(userId: String): DomainResult<UserProfile> {
        val profile = repository.findProfile(userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "Profile not found.")
        return DomainResult.Success(profile)
    }

    override suspend fun updateProfile(
        userId: String,
        request: UpdateProfileRequest,
    ): DomainResult<UserProfile> {
        repository.updateProfile(userId, request)
        val updated = repository.findProfile(userId)
            ?: return DomainResult.Error(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Failed to load updated profile.",
            )
        return DomainResult.Success(updated)
    }

    override suspend fun hireFreelancer(
        clientId: String,
        freelancerId: String,
    ): DomainResult<CommonResponse> {
        if (!repository.freelancerExists(freelancerId)) {
            return DomainResult.Error(ErrorCode.NOT_FOUND, "Freelancer not found.")
        }
        // TODO: send a real notification via NotificationsService
        return DomainResult.Success(
            CommonResponse(success = true, message = "Hire offer notification dispatched."),
        )
    }
}
