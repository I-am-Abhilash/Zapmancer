package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.KycRepository
import org.koin.core.annotation.Factory

@Factory
class GetKycReceiptUseCase(
    private val repository: KycRepository,
) {
    suspend operator fun invoke(verificationId: String): Result<KycReceiptResponse, DataError.Network> = repository.getReceipt(verificationId)
}
