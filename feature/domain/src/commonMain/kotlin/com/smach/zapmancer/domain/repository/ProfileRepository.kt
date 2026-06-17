package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(userId: String): Flow<UserProfile>
    fun getMyProfile(): Flow<UserProfile>
    suspend fun updateProfile(name: String, bio: String, avatarUrl: String?): Result<Unit, DataError.Network>
}
