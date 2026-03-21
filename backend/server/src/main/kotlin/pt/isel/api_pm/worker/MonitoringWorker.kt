package pt.isel.api_pm.worker

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.MetricsService

class MonitoringWorker(
    private val monitoringService: MonitoringService,
    private val metricsService: MetricsService,
    private val endpointService: EndpointService
) {

    fun start(scope: CoroutineScope) {
        scope.launch {
            while (true) {

                val endpoints = endpointService.getAll()

                for (endpoint in endpoints) {
                    try {
                        val metric = monitoringService.checkEndpoint(endpoint.url)

                        metricsService.save(metric)

                        println("Checked ${endpoint.url} -> ${metric.statusCode} (${metric.latency}ms)")
                    } catch (e: Exception) {
                        println("Error checking ${endpoint.url}: ${e.message}")
                    }
                }

                delay(60000)
            }
        }
    }
}