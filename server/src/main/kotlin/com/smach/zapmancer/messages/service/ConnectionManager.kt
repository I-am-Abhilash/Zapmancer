package com.smach.zapmancer.messages.service

import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.messages.redis.RedisClientService
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Single
class ConnectionManager(
    private val redisClientService: RedisClientService? = null,
) {
    private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Maps userId -> set of active web socket sessions on this node
    private val userSessions = ConcurrentHashMap<String, CopyOnWriteArraySet<DefaultWebSocketServerSession>>()

    init {
        // Start listening for messages published by other server nodes in the Redis cluster
        redisClientService?.startSubscriber { targetUserId, frameJson ->
            scope.launch {
                if (targetUserId != null) {
                    sendLocally(targetUserId, frameJson)
                } else {
                    broadcastLocally(frameJson)
                }
            }
        }
    }

    fun registerSession(userId: String, session: DefaultWebSocketServerSession): Boolean {
        val sessions = userSessions.computeIfAbsent(userId) { CopyOnWriteArraySet() }
        val isFirst = sessions.isEmpty()
        sessions.add(session)
        logger.info("Registered WS session for user $userId (Total active on this node: ${sessions.size})")

        redisClientService?.setPresence(userId, isOnline = true)
        return isFirst
    }

    fun deregisterSession(userId: String, session: DefaultWebSocketServerSession): Boolean {
        val sessions = userSessions[userId]
        if (sessions != null) {
            sessions.remove(session)
            if (sessions.isEmpty()) {
                userSessions.remove(userId)
                logger.info("Deregistered all WS sessions for user $userId on this node")
                redisClientService?.setPresence(userId, isOnline = false)
                return true
            }
        }
        return false
    }

    fun isUserOnline(userId: String): Boolean {
        if (userSessions[userId]?.isNotEmpty() == true) return true
        return redisClientService?.isUserOnline(userId) == true
    }

    fun setTyping(conversationId: String, userId: String, isTyping: Boolean) {
        redisClientService?.setTyping(conversationId, userId, isTyping)
    }

    suspend fun sendToUser(userId: String, frame: ChatFrame.ServerToClient, publishToRedis: Boolean = true) {
        val jsonText = Json.encodeToString<ChatFrame.ServerToClient>(frame)
        sendLocally(userId, jsonText)

        if (publishToRedis) {
            redisClientService?.publish(targetUserId = userId, frameJson = jsonText)
        }
    }

    suspend fun broadcast(frame: ChatFrame.ServerToClient, publishToRedis: Boolean = true) {
        val jsonText = Json.encodeToString<ChatFrame.ServerToClient>(frame)
        broadcastLocally(jsonText)

        if (publishToRedis) {
            redisClientService?.publish(targetUserId = null, frameJson = jsonText)
        }
    }

    private suspend fun sendLocally(userId: String, jsonText: String) {
        val sessions = userSessions[userId] ?: return
        sessions.forEach { session ->
            try {
                session.send(jsonText)
            } catch (e: Exception) {
                logger.error("Failed to send frame to user $userId on local node: ${e.message}")
            }
        }
    }

    private suspend fun broadcastLocally(jsonText: String) {
        userSessions.values.forEach { sessions ->
            sessions.forEach { session ->
                try {
                    session.send(jsonText)
                } catch (e: Exception) {
                    logger.error("Failed to broadcast frame on local node: ${e.message}")
                }
            }
        }
    }
}
