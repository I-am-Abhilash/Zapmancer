package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.KycRepository
import org.koin.core.annotation.Factory

@Factory
class SubmitKycUseCase(
    private val repository: KycRepository,
) {
    suspend operator fun invoke(
        verificationId: String,
        livenessSessionId: String,
        documentType: KycDocumentType,
        frontBytes: ByteArray,
        backBytes: ByteArray? = null,
        selfieBytes: ByteArray,
    ): Result<KycStatusResponse, DataError.Network> = repository.submitKyc(
        verificationId = verificationId,
        livenessSessionId = livenessSessionId,
        documentType = documentType,
        frontBytes = frontBytes,
        backBytes = backBytes,
        selfieBytes = selfieBytes,
    )
}
