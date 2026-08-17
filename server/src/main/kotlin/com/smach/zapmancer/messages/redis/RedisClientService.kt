package com.smach.zapmancer.messages.redis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub
import java.io.Closeable

@Serializable
data class RedisChatEnvelope(
    val targetUserId: String?, // null means broadcast
    val frameJson: String
)

class RedisClientService(
    private val host: String = "localhost",
    private val port: Int = 6379,
    private val password: String? = null
) : Closeable {
    private val logger = LoggerFactory.getLogger(RedisClientService::class.java)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val channelName = "zapmancer:chat:events"

    private val pool: JedisPool? by lazy {
        try {
            val config = JedisPoolConfig().apply {
                maxTotal = 32
                maxIdle = 16
                minIdle = 4
                testOnBorrow = false
            }
            if (password.isNullOrBlank()) {
                JedisPool(config, host, port, 3000)
            } else {
                JedisPool(config, host, port, 3000, password)
            }
        } catch (e: Exception) {
            logger.warn("Redis pool initialization failed (running in fallback in-memory mode): ${e.message}")
            null
        }
    }

    /**
     * Start background subscriber thread to listen for chat events from other server instances.
     */
    fun startSubscriber(onMessageReceived: (targetUserId: String?, frameJson: String) -> Unit) {
        scope.launch {
            try {
                pool?.resource?.use { jedis ->
                    logger.info("Subscribing to Redis channel '$channelName'")
                    jedis.subscribe(object : JedisPubSub() {
                        override fun onMessage(channel: String?, message: String?) {
                            if (message != null) {
                                try {
                                    val envelope = Json.decodeFromString<RedisChatEnvelope>(message)
                                    onMessageReceived(envelope.targetUserId, envelope.frameJson)
                                } catch (e: Exception) {
                                    logger.error("Failed to decode Redis chat envelope: ${e.message}")
                                }
                            }
                        }
                    }, channelName)
                }
            } catch (e: Exception) {
                logger.warn("Redis subscription disconnected: ${e.message}")
            }
        }
    }

    /**
     * Publish a chat frame envelope to the Redis cluster channel.
     */
    fun publish(targetUserId: String?, frameJson: String) {
        try {
            val envelope = RedisChatEnvelope(targetUserId = targetUserId, frameJson = frameJson)
            val envelopeJson = Json.encodeToString(envelope)
            pool?.resource?.use { jedis ->
                jedis.publish(channelName, envelopeJson)
            }
        } catch (e: Exception) {
            logger.warn("Failed to publish to Redis: ${e.message}")
        }
    }

    /**
     * Set ephemeral typing indicator in Redis with 3-second TTL.
     */
    fun setTyping(conversationId: String, userId: String, isTyping: Boolean) {
        try {
            pool?.resource?.use { jedis ->
                val key = "typing:$conversationId:$userId"
                if (isTyping) {
                    jedis.setex(key, 3, "1")
                } else {
                    jedis.del(key)
                }
            }
        } catch (e: Exception) {
            logger.debug("Redis setTyping failed: ${e.message}")
        }
    }

    /**
     * Set user online presence in Redis with a 45-second heartbeat TTL.
     */
    fun setPresence(userId: String, isOnline: Boolean) {
        try {
            pool?.resource?.use { jedis ->
                val key = "presence:$userId"
                if (isOnline) {
                    jedis.setex(key, 45, "1")
                } else {
                    jedis.del(key)
                }
            }
        } catch (e: Exception) {
            logger.debug("Redis setPresence failed: ${e.message}")
        }
    }

    /**
     * Check if a user is online across the cluster.
     */
    fun isUserOnline(userId: String): Boolean {
        return try {
            pool?.resource?.use { jedis ->
                jedis.exists("presence:$userId")
            } ?: false
        } catch (_: Exception) {
            false
        }
    }

    override fun close() {
        try {
            pool?.close()
        } catch (e: Exception) {
            logger.error("Error closing Redis pool: ${e.message}")
        }
    }
}
