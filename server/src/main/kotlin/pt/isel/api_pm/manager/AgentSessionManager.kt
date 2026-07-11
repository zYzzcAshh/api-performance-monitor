package pt.isel.api_pm.manager

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.metrics.toAgentEndpointMetrics
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.message.ServerMessage
import pt.isel.api_pm.repo.MetricsRepository
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.NotificationService
import pt.isel.api_pm.utils.AlertPipeline
import pt.isel.api_pm.utils.CooldownManager
import pt.isel.api_pm.utils.MetricsEventBus
import java.util.concurrent.ConcurrentHashMap

class AgentSessionManager(
    private val metricsService: MetricsService,
    private val agentService: AgentService,
    private val metricsEventBus: MetricsEventBus,
    private val alertPipeline: AlertPipeline,
) {
    private val logger = LoggerFactory.getLogger(AgentSessionManager::class.java)

    private val sessions = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, DefaultWebSocketSession>>()

    suspend fun register(userId: UInt, agentId: UInt, session: DefaultWebSocketServerSession) {
        val userSessions = sessions.computeIfAbsent(userId) { ConcurrentHashMap() }
        userSessions[agentId] = session
        agentService.activeAgent(userId, agentId)
    }

    suspend fun unregister(userId: UInt, agentId: UInt) {
        val userSessions = sessions[userId] ?: return
        userSessions.remove(agentId)
        if (userSessions.isEmpty()) {
            sessions.remove(userId)
        }
        agentService.inactiveAgent(userId, agentId)
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
            is AgentMessage.Metrics -> {
                logger.info("Received metrics message: {}", msg)
                metricsService.saveAgentMetrics(userId, agentId, msg.toAgentEndpointMetrics())
                metricsEventBus.publishAgent(userId, agentId, msg.toAgentEndpointMetrics())

                val agentEndpoint = agentService.getByIds(userId, agentId)?.endpoint
                val rule = agentEndpoint?.alertRule

                if (agentEndpoint != null && rule != null) {
                    alertPipeline.processAgent(
                        userId = userId,
                        agentId = agentId,
                        endpointLabel = agentEndpoint.name,
                        alertRule = rule,
                        notification = agentEndpoint.notification,
                        fetchHistory = { metricsService.getByAgent(userId, agentId) }
                    )
                }
            }
            is AgentMessage.Error -> logger.warn("Received error from agent $agentId for user $userId: ${msg.message}")
        }
    }
}