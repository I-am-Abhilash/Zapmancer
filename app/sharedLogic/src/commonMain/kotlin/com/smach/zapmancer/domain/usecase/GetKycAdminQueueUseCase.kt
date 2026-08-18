package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.KycRepository
import org.koin.core.annotation.Factory

@Factory
class GetKycAdminQueueUseCase(
    private val repository: KycRepository,
) {
    suspend operator fun invoke(): Result<List<KycAdminQueueItem>, DataError.Network> =
        repository.getAdminQueue()
}
