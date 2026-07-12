package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile as UserProfileDto
import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class ProfileRepositoryImpl(
    private val client: HttpClient,
) : ProfileRepository {

    override suspend fun getProfile(userId: String?): Result<UserProfile, DataError.Network> {
        val path = if (userId.isNullOrEmpty()) "users/profile" else "users/profile/$userId"
        return safeApiCall<UserProfileDto> { client.get(path) }.let { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.toDomain())
                is Result.Error -> result
            }
        }
    }

    override suspend fun hireUser(userId: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("users/$userId/hire")
    }.toUnitResult()

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfile, DataError.Network> = safeApiCall<UserProfileDto> {
        client.put("users/profile") {
            setBody(request)
        }
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> result
        }
    }
}

private fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    name = name,
    role = role,
    location = location.orEmpty(),
    ranking = ranking.orEmpty(),
    isTopRated = isTopRated,
    projectsCount = projectsCount,
    rating = rating,
    experience = experience.orEmpty(),
    about = about.orEmpty(),
    skills = skills,
    portfolioItems = portfolioItems.map { it.toDomain() },
    reviews = reviews.map { it.toDomain() },
    avatarUrl = avatarUrl,
)

private fun com.smach.zapmancer.core.common.dto.PortfolioItem.toDomain(): com.smach.zapmancer.domain.model.PortfolioItem =
    com.smach.zapmancer.domain.model.PortfolioItem(
        id = id.toString(),
        title = title,
        description = description.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
    )

private fun com.smach.zapmancer.core.common.dto.Review.toDomain(): com.smach.zapmancer.domain.model.ProfileReview =
    com.smach.zapmancer.domain.model.ProfileReview(
        authorId = authorId,
        id = id.toString(),
        authorName = authorName,
        authorRole = authorRole,
        content = content,
        rating = rating,
        authorAvatarUrl = authorAvatarUrl,
    )
