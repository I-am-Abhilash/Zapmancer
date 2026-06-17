package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.data.mapper.toDomain
import com.smach.zapmancer.data.model.UserProfileDto
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
) : ProfileRepository {

    override fun getUserProfile(userId: String): Flow<UserProfile> = flow {
        val result = safeApiCall<UserProfileDto> {
            httpClient.get("users/$userId/profile")
        }
        if (result is Result.Success) {
            emit(result.data.toDomain())
        }
    }

    override fun getMyProfile(): Flow<UserProfile> = flow {
        val result = safeApiCall<UserProfileDto> {
            httpClient.get("users/me")
        }
        if (result is Result.Success) {
            emit(result.data.toDomain())
        }
    }

    override suspend fun updateProfile(
        name: String,
        bio: String,
        avatarUrl: String?,
    ): Result<Unit, DataError.Network> {
        return safeApiCall<Unit> {
            httpClient.put("users/profile") {
                setBody(
                    UserProfileDto(
                        id = "", // ID is determined by token on backend
                        name = name,
                        bio = bio,
                        avatarUrl = avatarUrl
                    )
                )
            }
        }
    }
}
