package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.dto.ExecuteActionRequest
import com.smach.zapmancer.core.common.dto.SendQuickReplyRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.NotificationItem
import com.smach.zapmancer.domain.model.NotificationType
import com.smach.zapmancer.domain.repository.NotificationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import com.smach.zapmancer.core.common.dto.NotificationItem as NotificationItemDto

class NotificationRepositoryImpl(
    private val client: HttpClient,
) : NotificationRepository {

    override suspend fun getNotifications(): Result<List<NotificationItem>, DataError.Network> = safeApiCall<List<NotificationItemDto>> {
        client.get("notifications")
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            is Result.Error -> result
        }
    }

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

private fun NotificationItemDto.toDomain(): NotificationItem = NotificationItem(
    id = id.toString(),
    type = try {
        NotificationType.valueOf(type)
    } catch (e: Exception) {
        NotificationType.UNKNOWN
    },
    title = title,
    description = description,
    timestamp = timestamp,
    section = section,
    codeSnippet = codeSnippet,
    isItalic = isItalic,
    actions = actions.map { it.toDomain() },
    quickReply = quickReply,
    isRead = false,
)

private fun com.smach.zapmancer.core.common.dto.NotificationAction.toDomain(): com.smach.zapmancer.domain.model.NotificationAction = com.smach.zapmancer.domain.model.NotificationAction(
    label = label,
    isPrimary = isPrimary,
    isError = isError,
)
