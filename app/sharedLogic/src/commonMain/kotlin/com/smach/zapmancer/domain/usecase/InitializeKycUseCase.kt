package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycLivenessPreset
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.KycRepository
import org.koin.core.annotation.Factory

@Factory
class InitializeKycUseCase(
    private val repository: KycRepository,
) {
    suspend operator fun invoke(
        documentType: KycDocumentType,
        livenessPreset: KycLivenessPreset = KycLivenessPreset.EYE,
    ): Result<KycInitResponse, DataError.Network> = repository.initializeKyc(documentType, livenessPreset)
}
