package pt.isel.api_pm.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService

class MonitoringWorker(
    private val monitoringService: MonitoringService,
    private val metricsService: MetricsService,
    private val endpointService: EndpointService,
) {
    companion object {
        const val MINIMUM_INTERVAL_MILLIS: Long = 60000
    }

    fun start(scope: CoroutineScope) {
        scope.launch {
            while (true) {
                val endpoints = endpointService.getAll()

                val jobs =
                    endpoints.map { endpoint ->
                        async {
                            try {
                                val metric = monitoringService.checkEndpoint(endpoint.url)
                                metricsService.save(endpoint.userId, endpoint.id, metric)
                                println("Saved metric for userId=${endpoint.userId}, endpointId=${endpoint.id}, url=${endpoint.url}")
                                println("Checked ${endpoint.url} -> ${metric.statusCode} (${metric.latency}ms)")
                            } catch (e: Exception) {
                                println("Error checking ${endpoint.url}: ${e.message}")
                            }
                        }
                    }

                jobs.awaitAll()

                delay(MINIMUM_INTERVAL_MILLIS)
            }
        }
    }
}
