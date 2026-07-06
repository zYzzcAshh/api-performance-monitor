package pt.isel.api_pm.utils

import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.service.NotificationService
import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.notification.NotificationConfig

class AlertPipeline(
    private val alertEvaluator: AlertEvaluator,
    private val notificationService: NotificationService,
    private val cooldownManager: CooldownManager,
) {
    private val logger = LoggerFactory.getLogger(AlertPipeline::class.java)

    suspend fun processEndpoint(
        userId: UInt,
        endpointId: UInt,
        endpointLabel: String,
        alertRule: AlertRule?,
        notification: NotificationConfig,
        fetchHistory: suspend () -> List<EndpointMetrics>,
    ) {
        if (alertRule == null) return

        if (cooldownManager.isInCooldown(userId, endpointId)) {
            logger.info("Endpoint $endpointLabel is in cooldown. Skipping alert evaluation.")
            return
        }

        logger.info("Evaluating alert for $endpointLabel with rule $alertRule")

        val history = fetchHistory()
        val triggered = alertEvaluator.shouldTrigger(history, alertRule)

        if (triggered) {
            logger.info("Alert triggered for $endpointLabel with rule $alertRule, not=$notification")
            notificationService.notifyAll(notification, endpointLabel)
            cooldownManager.markCooldown(userId, endpointId)
        } else {
            logger.info("No alert triggered for $endpointLabel with rule $alertRule")
        }
    }

    suspend fun processAgent(
        userId: UInt,
        agentId: UInt,
        endpointLabel: String,
        alertRule: AlertRule?,
        notification: NotificationConfig,
        fetchHistory: suspend () -> List<AgentEndpointMetrics>,
    ) {
        if (alertRule == null) return

        if (cooldownManager.isInCooldownAgent(userId, agentId)) {
            logger.info("Agent endpoint $endpointLabel is in cooldown. Skipping alert evaluation.")
            return
        }

        logger.info("Evaluating agent alert for $endpointLabel with rule $alertRule")

        val history = fetchHistory()
        val triggered = alertEvaluator.shouldTriggerAgent(history, alertRule)

        if (triggered) {
            logger.info("Alert triggered for $endpointLabel with rule $alertRule, not=$notification")
            notificationService.notifyAll(notification, endpointLabel)
            cooldownManager.markAgentCooldown(userId, agentId)
        } else {
            logger.info("No alert triggered for $endpointLabel with rule $alertRule")
        }
    }
}