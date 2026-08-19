package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.KycRepository
import org.koin.core.annotation.Factory

@Factory
class GetKycStatusUseCase(
    private val repository: KycRepository,
) {
    suspend operator fun invoke(): Result<KycStatusResponse, DataError.Network> = repository.getKycStatus()
}
