package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UpdateProfileParams
import com.smach.zapmancer.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(userId: String? = null): Result<UserProfile, DataError.Network>
    suspend fun hireUser(userId: String): Result<Unit, DataError.Network>
    suspend fun updateProfile(params: UpdateProfileParams): Result<UserProfile, DataError.Network>
    suspend fun deleteAccount(): Result<Unit, DataError.Network>
    suspend fun submitReview(userId: String, content: String, rating: Int): Result<Unit, DataError.Network>
    suspend fun getReviews(userId: String, page: Int? = null, limit: Int? = null): Result<List<com.smach.zapmancer.core.common.dto.Review>, DataError.Network>
}
