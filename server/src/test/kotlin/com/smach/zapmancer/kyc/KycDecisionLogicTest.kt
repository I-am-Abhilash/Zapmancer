package com.smach.zapmancer.kyc

import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.kyc.service.KycService
import kotlin.test.Test
import kotlin.test.assertEquals

class KycDecisionLogicTest {

    @Test
    fun testAutoApproveWhenHighSimilarityAndLivenessPassedAndNameMatched() {
        val decision = KycService.evaluateDecision(
            similarity = 0.91,
            livenessPassed = true,
            isNameMatched = true
        )
        assertEquals(KycStatus.VERIFIED, decision)
    }

    @Test
    fun testManualReviewWhenSimilarityIsAmbiguous() {
        val decision = KycService.evaluateDecision(
            similarity = 0.72,
            livenessPassed = true,
            isNameMatched = true
        )
        assertEquals(KycStatus.MANUAL_REVIEW, decision)
    }

    @Test
    fun testRejectWhenLivenessFailed() {
        val decision = KycService.evaluateDecision(
            similarity = 0.95,
            livenessPassed = false,
            isNameMatched = true
        )
        assertEquals(KycStatus.FAILED, decision)
    }

    @Test
    fun testRejectWhenSimilarityIsTooLow() {
        val decision = KycService.evaluateDecision(
            similarity = 0.45,
            livenessPassed = true,
            isNameMatched = true
        )
        assertEquals(KycStatus.FAILED, decision)
    }
}
