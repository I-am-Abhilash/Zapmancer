package com.smach.zapmancer.kyc.repository

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.core.database.KycVerificationsTable
import com.smach.zapmancer.core.database.ProjectsTable
import com.smach.zapmancer.core.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock.System

class KycRepository {

    suspend fun createVerification(
        id: String,
        userId: String,
        documentType: KycDocumentType,
        documentFrontUrl: String,
        documentBackUrl: String?,
        selfieUrl: String,
        status: KycStatus = KycStatus.PENDING
    ): KycStatusResponse = dbQuery {
        val now = System.now().toLocalDateTime(TimeZone.UTC)
        KycVerificationsTable.insert {
            it[KycVerificationsTable.id] = id
            it[KycVerificationsTable.userId] = userId
            it[KycVerificationsTable.documentType] = documentType.name
            it[KycVerificationsTable.documentFrontUrl] = documentFrontUrl
            it[KycVerificationsTable.documentBackUrl] = documentBackUrl
            it[KycVerificationsTable.selfieUrl] = selfieUrl
            it[KycVerificationsTable.status] = status.name
            it[KycVerificationsTable.createdAt] = now
            it[KycVerificationsTable.updatedAt] = now
        }
        getVerificationById(id)!!
    }

    suspend fun updateVerificationResult(
        id: String,
        status: KycStatus,
        extractedName: String?,
        extractedDob: String?,
        extractedDocNumber: String?,
        extractedExpiry: String?,
        faceSimilarityScore: Double?,
        livenessScore: Double?,
        livenessPassed: Boolean,
        isNameMatched: Boolean,
        receiptSignature: String?,
        receiptHash: String?,
        reviewerNotes: String? = null
    ): KycStatusResponse? = dbQuery {
        val now = System.now().toLocalDateTime(TimeZone.UTC)
        KycVerificationsTable.update({ KycVerificationsTable.id eq id }) {
            it[KycVerificationsTable.status] = status.name
            it[KycVerificationsTable.extractedName] = extractedName
            it[KycVerificationsTable.extractedDob] = extractedDob
            it[KycVerificationsTable.extractedDocNumber] = extractedDocNumber
            it[KycVerificationsTable.extractedExpiry] = extractedExpiry
            it[KycVerificationsTable.faceSimilarityScore] = faceSimilarityScore
            it[KycVerificationsTable.livenessScore] = livenessScore
            it[KycVerificationsTable.livenessPassed] = livenessPassed
            it[KycVerificationsTable.isNameMatched] = isNameMatched
            it[KycVerificationsTable.receiptSignature] = receiptSignature
            it[KycVerificationsTable.receiptHash] = receiptHash
            if (reviewerNotes != null) {
                it[KycVerificationsTable.reviewerNotes] = reviewerNotes
            }
            it[KycVerificationsTable.updatedAt] = now
        }
        getVerificationById(id)
    }

    suspend fun getVerificationById(id: String): KycStatusResponse? = dbQuery {
        KycVerificationsTable.selectAll()
            .where { KycVerificationsTable.id eq id }
            .map(::rowToKycStatusResponse)
            .singleOrNull()
    }

    suspend fun getLatestVerificationForUser(userId: String): KycStatusResponse? = dbQuery {
        KycVerificationsTable.selectAll()
            .where { KycVerificationsTable.userId eq userId }
            .orderBy(KycVerificationsTable.createdAt, SortOrder.DESC)
            .limit(1)
            .map(::rowToKycStatusResponse)
            .singleOrNull()
    }

    suspend fun getAdminReviewQueue(): List<KycAdminQueueItem> = dbQuery {
        (KycVerificationsTable innerJoin UsersTable)
            .selectAll()
            .where { KycVerificationsTable.status inList listOf(KycStatus.MANUAL_REVIEW.name, KycStatus.PENDING.name) }
            .orderBy(KycVerificationsTable.createdAt, SortOrder.ASC)
            .map { row ->
                KycAdminQueueItem(
                    verificationId = row[KycVerificationsTable.id],
                    userId = row[KycVerificationsTable.userId],
                    username = row[UsersTable.username],
                    email = row[UsersTable.email],
                    documentType = KycDocumentType.valueOf(row[KycVerificationsTable.documentType]),
                    documentFrontUrl = row[KycVerificationsTable.documentFrontUrl],
                    documentBackUrl = row[KycVerificationsTable.documentBackUrl],
                    selfieUrl = row[KycVerificationsTable.selfieUrl],
                    extractedName = row[KycVerificationsTable.extractedName],
                    faceSimilarityScore = row[KycVerificationsTable.faceSimilarityScore],
                    livenessScore = row[KycVerificationsTable.livenessScore],
                    livenessPassed = row[KycVerificationsTable.livenessPassed],
                    isNameMatched = row[KycVerificationsTable.isNameMatched],
                    status = KycStatus.valueOf(row[KycVerificationsTable.status]),
                    createdAt = row[KycVerificationsTable.createdAt].toString()
                )
            }
    }

    suspend fun updateAdminReview(
        id: String,
        adminUserId: String,
        decision: KycStatus,
        notes: String?
    ): KycStatusResponse? = dbQuery {
        val now = System.now().toLocalDateTime(TimeZone.UTC)
        KycVerificationsTable.update({ KycVerificationsTable.id eq id }) {
            it[KycVerificationsTable.status] = decision.name
            it[KycVerificationsTable.reviewerNotes] = notes
            it[KycVerificationsTable.reviewedBy] = adminUserId
            it[KycVerificationsTable.updatedAt] = now
        }
        getVerificationById(id)
    }

    suspend fun updateProjectIdentityFlags(userId: String, isVerified: Boolean) = dbQuery {
        ProjectsTable.update({ ProjectsTable.clientId eq userId }) {
            it[ProjectsTable.isIdentityVerified] = isVerified
        }
    }

    private fun rowToKycStatusResponse(row: ResultRow) = KycStatusResponse(
        verificationId = row[KycVerificationsTable.id],
        userId = row[KycVerificationsTable.userId],
        status = KycStatus.valueOf(row[KycVerificationsTable.status]),
        documentType = KycDocumentType.valueOf(row[KycVerificationsTable.documentType]),
        extractedName = row[KycVerificationsTable.extractedName],
        extractedDob = row[KycVerificationsTable.extractedDob],
        extractedDocNumber = row[KycVerificationsTable.extractedDocNumber],
        extractedExpiry = row[KycVerificationsTable.extractedExpiry],
        faceSimilarityScore = row[KycVerificationsTable.faceSimilarityScore],
        livenessScore = row[KycVerificationsTable.livenessScore],
        livenessPassed = row[KycVerificationsTable.livenessPassed],
        isNameMatched = row[KycVerificationsTable.isNameMatched],
        receiptSignature = row[KycVerificationsTable.receiptSignature],
        receiptHash = row[KycVerificationsTable.receiptHash],
        reviewerNotes = row[KycVerificationsTable.reviewerNotes],
        createdAt = row[KycVerificationsTable.createdAt].toString(),
        updatedAt = row[KycVerificationsTable.updatedAt].toString()
    )
}
