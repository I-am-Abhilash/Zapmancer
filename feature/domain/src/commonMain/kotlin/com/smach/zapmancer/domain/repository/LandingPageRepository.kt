package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.LandingPageData

interface LandingPageRepository {
    suspend fun getLandingPageData(): Result<LandingPageData, DataError.Network>
}
