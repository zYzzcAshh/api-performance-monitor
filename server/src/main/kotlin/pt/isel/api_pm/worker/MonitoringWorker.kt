package pt.isel.api_pm.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.NotificationService
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MonitoringWorker(
    private val monitoringService: MonitoringService,
    private val metricsService: MetricsService,
    private val endpointService: EndpointService,
    private val agentService: AgentService,
    private val agentSessionManager: AgentSessionManager,
    private val notificationService: NotificationService,
    private val alertEvaluator: AlertEvaluator,
    private val intervalSeconds: Long,
) {

    private val logger =
        LoggerFactory.getLogger(
            "MonitoringWorker-${intervalSeconds}s"
        )

    private val cooldownMs =
        15.minutes.inWholeMilliseconds

    private val cooldownMap =
        ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, Long>>()

    fun start(scope: CoroutineScope) {

        logger.info(
            "Starting worker for ${intervalSeconds}s..."
        )

        scope.launch {

            while (isActive) {

                val endpoints =
                    endpointService.getAll()

                val agentEndpoints = agentService.getAll()

                coroutineScope {

                    endpoints.forEach { endpoint ->

                        if (endpoint.interval.value != intervalSeconds) {
                            return@forEach
                        }

                        launch {

                            try {

                                val metric =
                                    monitoringService.checkEndpoint(
                                        endpoint
                                    )

                                metricsService.save(
                                    endpoint.userId,
                                    endpoint.id,
                                    metric
                                )

                                if (endpoint.alertRule != null) {

                                    val alertRule: AlertRule =
                                        endpoint.alertRule ?: return@launch

                                    if (
                                        isInCooldown(
                                            endpoint.userId,
                                            endpoint.id
                                        )
                                    ) {

                                        logger.info(
                                            "Endpoint ${endpoint.url.value} is in cooldown. Skipping alert evaluation."
                                        )

                                    } else {

                                        logger.info(
                                            "Evaluating alert for ${endpoint.url.value} with rule $alertRule"
                                        )

                                        val history =
                                            metricsService.getMetricsHistoryByAlert(
                                                endpoint.userId,
                                                endpoint.id,
                                                alertRule
                                            )

                                        val alert =
                                            alertEvaluator.shouldTrigger(
                                                history,
                                                alertRule
                                            )

                                        if (alert) {

                                            logger.info(
                                                "Alert triggered for ${endpoint.url.value} with rule $alertRule, not=${endpoint.notification}"
                                            )

                                            notificationService.notifyAll(
                                                endpoint.notification,
                                                endpoint.name
                                            )

                                            markCooldown(
                                                endpoint.userId,
                                                endpoint.id
                                            )

                                        } else {

                                            logger.info(
                                                "No alert triggered for ${endpoint.url.value} with rule $alertRule"
                                            )
                                        }
                                    }
                                }

                                logger.info(
                                    "Saved metric for userId=${endpoint.userId}, endpointId=${endpoint.id}, url=${endpoint.url.value}"
                                )

                                logger.info(
                                    "Checked ${endpoint.url.value} -> ${metric.statusCode} (${metric.latency}ms)"
                                )

                            } catch (e: Exception) {

                                logger.error(
                                    "Error checking ${endpoint.url.value}: ${e.message}"
                                )
                            }
                        }
                    }

                    agentEndpoints.forEach { endpoint ->
                        if (endpoint.endpoint?.intervalSeconds?.value != intervalSeconds) {
                            return@forEach
                        }
                        logger.info("GOOD REQUEST WORKED FOR $endpoint !!!!!!!!!!!!!")

                        launch {
                            agentSessionManager.sendDoRequest(
                                endpoint.userId,
                                endpoint.id,
                                endpoint.name
                            )
                        }
                    }
                }

                delay(intervalSeconds.seconds)
            }
        }
    }

    private fun markCooldown(
        userId: UInt,
        endpointId: UInt
    ) {

        val userCooldowns =
            cooldownMap.getOrPut(userId) {
                ConcurrentHashMap()
            }

        userCooldowns[endpointId] =
            System.currentTimeMillis()
    }

    private fun isInCooldown(
        userId: UInt,
        endpointId: UInt
    ): Boolean {

        val userCooldowns =
            cooldownMap[userId] ?: return false

        val cooldownEnd =
            userCooldowns[endpointId] ?: return false

        val now =
            System.currentTimeMillis()

        return (now - cooldownEnd) < cooldownMs
    }
}