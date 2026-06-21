package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.serialization.Serializable

class ProfileRepositoryImpl(
    private val client: HttpClient
) : ProfileRepository {

    override suspend fun getProfile(userId: String?): Result<UserProfile, DataError.Network> {
        val path = if (userId.isNullOrEmpty()) "users/profile" else "users/profile/$userId"
        return safeApiCall<UserProfile> {
            client.get(path)
        }
    }

    override suspend fun hireUser(userId: String): Result<Unit, DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("users/$userId/hire")
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }
}
