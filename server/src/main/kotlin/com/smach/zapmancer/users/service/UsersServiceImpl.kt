package com.smach.zapmancer.users.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.users.repository.UsersRepository
import org.koin.core.annotation.Single

@Single(binds = [UsersService::class])
class UsersServiceImpl(
    private val repository: UsersRepository,
) : UsersService {

    override suspend fun getOwnProfile(userId: String): UserProfile = repository.findProfile(userId)
        ?: throw ApiException(ErrorCode.NOT_FOUND, "Profile not found.")

    override suspend fun getPublicProfile(userId: String): UserProfile = repository.findProfile(userId)
        ?: throw ApiException(ErrorCode.NOT_FOUND, "Profile not found.")

    override suspend fun updateProfile(
        userId: String,
        request: UpdateProfileRequest,
    ): UserProfile {
        repository.updateProfile(userId, request)
        return repository.findProfile(userId)
            ?: throw ApiException(
                ErrorCode.NOT_FOUND,
                "User profile not found.",
            )
    }

    override suspend fun hireFreelancer(
        clientId: String,
        freelancerId: String,
    ): CommonResponse {
        if (!repository.freelancerExists(freelancerId)) {
            throw ApiException(ErrorCode.NOT_FOUND, "Freelancer not found.")
        }
        return CommonResponse(success = true, message = "Hire offer notification dispatched.")
    }

    override suspend fun createReview(
        authorId: String,
        subjectId: String,
        request: CreateReviewRequest,
    ): Review {
        if (request.content.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Review content cannot be blank.")
        }
        if (request.rating !in 1..5) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Rating must be an integer between 1 and 5.")
        }
        if (authorId == subjectId) {
            throw ApiException(ErrorCode.BAD_REQUEST, "You cannot review your own profile.")
        }
        if (!repository.freelancerExists(subjectId)) {
            throw ApiException(ErrorCode.NOT_FOUND, "Target user not found.")
        }
        return repository.createReview(authorId, subjectId, request)
    }

    override suspend fun getReviews(subjectId: String, limit: Int, offset: Long): List<Review> {
        if (!repository.freelancerExists(subjectId)) {
            throw ApiException(ErrorCode.NOT_FOUND, "Target user not found.")
        }
        return repository.getReviews(subjectId, limit, offset)
    }

    override suspend fun deactivateAccount(userId: String): CommonResponse {
        val success = repository.deactivateAccount(userId)
        if (!success) {
            throw ApiException(ErrorCode.NOT_FOUND, "User account not found.")
        }
        return CommonResponse(success = true, message = "Account deactivated successfully.")
    }
}
