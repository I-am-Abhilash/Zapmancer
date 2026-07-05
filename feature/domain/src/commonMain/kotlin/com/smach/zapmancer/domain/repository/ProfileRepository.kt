package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(userId: String? = null): Result<UserProfile, DataError.Network>
    suspend fun hireUser(userId: String): Result<Unit, DataError.Network>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfile, DataError.Network>
}
