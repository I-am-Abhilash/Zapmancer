package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.UpdateProfileParams
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single
import com.smach.zapmancer.core.common.dto.UserProfile as UserProfileDto

@Single(binds = [ProfileRepository::class])
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

    override suspend fun updateProfile(params: UpdateProfileParams): Result<UserProfile, DataError.Network> = safeApiCall<UserProfileDto> {
        client.put("users/profile") {
            setBody(
                UpdateProfileRequest(
                    name = params.name,
                    roleTitle = params.roleTitle,
                    location = params.location,
                    about = params.about,
                    experience = params.experience,
                    skills = params.skills,
                    avatarUrl = params.avatarUrl,
                ),
            )
        }
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> result
        }
    }

    override suspend fun deleteAccount(): Result<Unit, DataError.Network> =
        safeApiCall<CommonResponse> { client.delete("users/account") }.toUnitResult()

    override suspend fun submitReview(
        userId: String,
        content: String,
        rating: Int,
    ): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("users/$userId/reviews") {
            setBody(CreateReviewRequest(content = content, rating = rating))
        }
    }.toUnitResult()

    override suspend fun getReviews(
        userId: String,
        page: Int?,
        limit: Int?,
    ): Result<List<Review>, DataError.Network> = safeApiCall<List<Review>> {
        client.get("users/$userId/reviews") {
            url {
                page?.let { parameters.append("page", it.toString()) }
                limit?.let { parameters.append("limit", it.toString()) }
            }
        }
    }
}

private fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    name = name,
    role = role,
    location = location,
    ranking = ranking,
    isTopRated = isTopRated,
    projectsCount = projectsCount,
    rating = rating,
    experience = experience,
    about = about,
    skills = skills,
    portfolioItems = portfolioItems.map { it.toDomain() },
    reviews = reviews.map { it.toDomain() },
    avatarUrl = avatarUrl,
)

private fun com.smach.zapmancer.core.common.dto.PortfolioItem.toDomain(): com.smach.zapmancer.domain.model.PortfolioItem = com.smach.zapmancer.domain.model.PortfolioItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
)

private fun com.smach.zapmancer.core.common.dto.Review.toDomain(): com.smach.zapmancer.domain.model.ProfileReview = com.smach.zapmancer.domain.model.ProfileReview(
    authorId = authorId,
    id = id.toString(),
    authorName = authorName,
    authorRole = authorRole,
    content = content,
    rating = rating,
    authorAvatarUrl = authorAvatarUrl,
)
