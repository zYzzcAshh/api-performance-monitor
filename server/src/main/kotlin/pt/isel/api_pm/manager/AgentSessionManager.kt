package pt.isel.api_pm.manager

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.serialization.json.Json
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.message.ServerMessage
import pt.isel.api_pm.repo.MetricsRepository
import java.util.concurrent.ConcurrentHashMap

class AgentSessionManager(
    private val metricsRepository: MetricsRepository
) {
    private val sessions = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, DefaultWebSocketSession>>()

    fun register(userId: UInt, agentId: UInt, session: DefaultWebSocketServerSession) {
        val userSessions = sessions.computeIfAbsent(userId) { ConcurrentHashMap() }
        userSessions[agentId] = session
    }

    fun unregister(userId: UInt, agentId: UInt) {
        val userSessions = sessions[userId] ?: return
        userSessions.remove(agentId)
        if (userSessions.isEmpty()) {
            sessions.remove(userId)
        }
    }

    suspend fun sendDoRequest(userId: UInt, agentId: UInt, endpointName: String) {
        val session = sessions[userId]?.get(agentId) ?: return
        try {
            session.send(Frame.Text(Json.encodeToString<ServerMessage>(ServerMessage.DoRequest(endpointName))))
        } catch (_: Exception) {
            unregister(userId, agentId)
        }
    }

    suspend fun handleIncoming(userId: UInt, agentId: UInt, frame: Frame.Text) {
        when (val msg = Json.decodeFromString<AgentMessage>(frame.readText())) {
            is AgentMessage.Metrics -> metricsRepository.saveAgentMetrics(userId, agentId, msg)
            is AgentMessage.Error -> println("Received error from agent $agentId for user $userId: ${msg.message}")
        }
    }
}