package com.smach.zapmancer.users.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.users.repository.UsersRepository
import org.koin.core.annotation.Single

@Single(binds = [UsersService::class])
class UsersServiceImpl(
    private val repository: UsersRepository,
) : UsersService {

    override suspend fun getOwnProfile(userId: String): UserProfile {
        return repository.findProfile(userId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Profile not found.")
    }

    override suspend fun getPublicProfile(userId: String): UserProfile {
        return repository.findProfile(userId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Profile not found.")
    }

    override suspend fun updateProfile(
        userId: String,
        request: UpdateProfileRequest,
    ): UserProfile {
        repository.updateProfile(userId, request)
        return repository.findProfile(userId)
            ?: throw ApiException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Failed to load updated profile.",
            )
    }

    override suspend fun hireFreelancer(
        clientId: String,
        freelancerId: String,
    ): CommonResponse {
        if (!repository.freelancerExists(freelancerId)) {
            throw ApiException(ErrorCode.NOT_FOUND, "Freelancer not found.")
        }
        // TODO: send a real notification via NotificationsService
        return CommonResponse(success = true, message = "Hire offer notification dispatched.")
    }
}

