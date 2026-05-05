package pt.isel.api_pm.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.NotificationService
import java.util.concurrent.ConcurrentHashMap

class MonitoringWorker(
    private val monitoringService: MonitoringService,
    private val metricsService: MetricsService,
    private val endpointService: EndpointService,
    private val notificationService: NotificationService,
    private val alertEvaluator: AlertEvaluator,
    private val intervalSeconds: Long,
) {
    private val logger = LoggerFactory.getLogger("MonitoringWorker-${intervalSeconds}s")

    private val COOLDOWN_MS = 15 * 60 * 1000 // 15 minutes

    private val cooldownMap = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, Long>>()

    fun start(scope: CoroutineScope) {
        logger.info("Starting worker for ${intervalSeconds}s...")

        scope.launch {
            while (isActive) {
                val endpoints = endpointService.getAll()

                coroutineScope {
                    endpoints.forEach { endpoint ->
                        if (endpoint.interval.value != intervalSeconds) return@forEach

                        launch {
                            try {
                                val metric = monitoringService.checkEndpoint(endpoint.url)

                                metricsService.save(endpoint.userId, endpoint.id, metric)

                                if (endpoint.alertRule != null) {
                                    if (isInCooldown(endpoint.userId, endpoint.id)) {
                                        logger.info(
                                            "Endpoint ${endpoint.url.value} is in cooldown. Skipping alert evaluation.",
                                        )
                                    } else {
                                        val history = metricsService.getMetricsHistoryByAlert(endpoint.userId, endpoint.id, endpoint.alertRule!!)
                                        val alert = alertEvaluator.shouldTrigger(history, endpoint.alertRule!!)
                                        if (alert) {
                                            notificationService.notifyAll(endpoint.notification)
                                            markCooldown(endpoint.userId, endpoint.id)
                                        }
                                    }
                                }

                                logger.info(
                                    "Saved metric for userId=${endpoint.userId}, endpointId=${endpoint.id}, url=${endpoint.url.value}",
                                )

                                logger.info(
                                    "Checked ${endpoint.url.value} -> ${metric.statusCode} (${metric.latency}ms)",
                                )
                            } catch (e: Exception) {
                                logger.error("Error checking ${endpoint.url.value}: ${e.message}")
                            }
                        }
                    }
                }

                delay(intervalSeconds * 1000)
            }
        }
    }

    private fun markCooldown(userId: UInt, endpointId: UInt) {
        val userCooldowns = cooldownMap.getOrPut(userId) { ConcurrentHashMap() }
        userCooldowns[endpointId] = System.currentTimeMillis()
    }

    private fun isInCooldown(userId: UInt, endpointId: UInt): Boolean {
        val userCooldowns = cooldownMap[userId] ?: return false
        val cooldownEnd = userCooldowns[endpointId] ?: return false
        val now = System.currentTimeMillis()
        return (now - cooldownEnd) < COOLDOWN_MS
    }
}
