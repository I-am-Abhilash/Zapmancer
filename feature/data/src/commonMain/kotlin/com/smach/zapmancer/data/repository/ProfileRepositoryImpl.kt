package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post

class ProfileRepositoryImpl(
    private val client: HttpClient,
) : ProfileRepository {

    override suspend fun getProfile(userId: String?): Result<UserProfile, DataError.Network> {
        val path = if (userId.isNullOrEmpty()) "users/profile" else "users/profile/$userId"
        return safeApiCall<UserProfile> { client.get(path) }
    }

    override suspend fun hireUser(userId: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> { client.post("users/$userId/hire") }.toUnitResult()
}
