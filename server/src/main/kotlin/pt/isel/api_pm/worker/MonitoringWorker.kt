package pt.isel.api_pm.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.NotificationService
import pt.isel.api_pm.utils.AlertPipeline
import pt.isel.api_pm.utils.CooldownManager
import pt.isel.api_pm.utils.MetricsEventBus
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MonitoringWorker(
    private val monitoringService: MonitoringService,
    private val metricsService: MetricsService,
    private val endpointService: EndpointService,
    private val agentService: AgentService,
    private val agentSessionManager: AgentSessionManager,
    private val alertPipeline: AlertPipeline,
    private val metricsEventBus: MetricsEventBus,
    private val intervalSeconds: Long,
) {

    private val logger =
        LoggerFactory.getLogger(
            "MonitoringWorker-${intervalSeconds}s"
        )

    fun start(scope: CoroutineScope) {
        val interval = IntervalSeconds(intervalSeconds)

        logger.info(
            "Starting worker for ${intervalSeconds}s..."
        )

        scope.launch {
            while (isActive) {
                val endpoints =
                    endpointService.getAllActiveByIntervalSeconds(interval)

                val agentEndpoints =
                    agentService.getAllActiveByIntervalSeconds(interval)

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

                                metricsEventBus.publishEndpoint(
                                    endpoint.userId,
                                    endpoint.id,
                                    metric
                                )

                                alertPipeline.processEndpoint(
                                    userId = endpoint.userId,
                                    endpointId = endpoint.id,
                                    endpointLabel = endpoint.url.value,
                                    alertRule = endpoint.alertRule,
                                    notification = endpoint.notification,
                                    fetchHistory = {
                                        metricsService.getMetricsHistoryByAlert(
                                            endpoint.userId,
                                            endpoint.id,
                                            endpoint.alertRule!!
                                        )
                                    }
                                )

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

                        launch {
                            agentSessionManager.sendDoRequest(
                                endpoint.userId,
                                endpoint.id,
                                endpoint.name
                            )

                            logger.info("Sent do request to agent ${endpoint.name} for endpoint ${endpoint.endpoint!!.name}")
                        }
                    }
                }

                delay(intervalSeconds.seconds)
            }
        }
    }
}