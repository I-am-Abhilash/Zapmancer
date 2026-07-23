package com.smach.zapmancer.messages.service

import com.smach.zapmancer.core.common.dto.ChatFrame
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.send
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

class ConnectionManager {
    private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)

    // Maps userId -> set of active web socket sessions
    private val userSessions = ConcurrentHashMap<String, CopyOnWriteArraySet<DefaultWebSocketServerSession>>()

    fun registerSession(userId: String, session: DefaultWebSocketServerSession): Boolean {
        val sessions = userSessions.computeIfAbsent(userId) { CopyOnWriteArraySet() }
        val isFirst = sessions.isEmpty()
        sessions.add(session)
        logger.info("Registered WS session for user $userId (Total active: ${sessions.size})")
        return isFirst
    }

    fun deregisterSession(userId: String, session: DefaultWebSocketServerSession): Boolean {
        val sessions = userSessions[userId]
        if (sessions != null) {
            sessions.remove(session)
            if (sessions.isEmpty()) {
                userSessions.remove(userId)
                logger.info("Deregistered all WS sessions for user $userId")
                return true
            }
        }
        return false
    }

    fun isUserOnline(userId: String): Boolean = userSessions[userId]?.isNotEmpty() == true

    suspend fun sendToUser(userId: String, frame: ChatFrame.ServerToClient) {
        val sessions = userSessions[userId] ?: return
        val jsonText = Json.encodeToString<ChatFrame.ServerToClient>(frame)
        sessions.forEach { session ->
            try {
                session.send(jsonText)
            } catch (e: Exception) {
                logger.error("Failed to send frame to user $userId: ${e.message}")
            }
        }
    }

    suspend fun broadcast(frame: ChatFrame.ServerToClient) {
        val jsonText = Json.encodeToString<ChatFrame.ServerToClient>(frame)
        userSessions.values.forEach { sessions ->
            sessions.forEach { session ->
                try {
                    session.send(jsonText)
                } catch (e: Exception) {
                    logger.error("Failed to broadcast frame: ${e.message}")
                }
            }
        }
    }
}
