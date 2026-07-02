package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.NotificationItem
import com.smach.zapmancer.domain.repository.NotificationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

class NotificationRepositoryImpl(
    private val client: HttpClient,
) : NotificationRepository {

    override suspend fun getNotifications(): Result<List<NotificationItem>, DataError.Network> = safeApiCall<List<NotificationItem>> { client.get("notifications") }

    override suspend fun executeAction(
        notificationId: String,
        actionLabel: String,
    ): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("notifications/$notificationId/action") {
            setBody(ExecuteActionRequest(actionLabel = actionLabel))
        }
    }.toUnitResult()

    override suspend fun sendQuickReply(
        notificationId: String,
        replyText: String,
    ): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("notifications/$notificationId/reply") {
            setBody(SendQuickReplyRequest(replyText = replyText))
        }
    }.toUnitResult()
}

@Serializable
private data class ExecuteActionRequest(val actionLabel: String)

@Serializable
private data class SendQuickReplyRequest(val replyText: String)
