package pt.isel.api_pm.service

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.dto.MetricsSummaryDTO
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class MetricsService(
    private val repo: MetricsRepository,
) {
    suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: RequestMetric,
    ) = repo.save(userId, monitoredEndpointId, metric)

    suspend fun getAll(): List<RequestMetric> = repo.getAll()

    suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<RequestMetric> = repo.getByEndpoint(userId, monitoredEndpointId)

    suspend fun getSummary(
        userId: UInt,
        endpointId: UInt,
    ): MetricsSummaryDTO {
        val metrics = getByEndpoint(userId, endpointId)

        if (metrics.isEmpty()) {
            return MetricsSummaryDTO(
                uptime = 0.0,
                averageLatency = 0.0,
                totalRequests = 0,
            )
        }

        val total = metrics.size

        val successCount = metrics.count { it.statusCode in 200..299 }

        val uptime = (successCount.toDouble() / total) * 100

        val avgLatency = metrics.map { it.latency }.average()

        return MetricsSummaryDTO(
            uptime = uptime,
            averageLatency = avgLatency,
            totalRequests = total,
        )
    }

    suspend fun getMetricsHistoryByAlert(
        userId: UInt,
        endpointId: UInt,
        alertRule: AlertRule
    ): List<RequestMetric> {
        val now = Clock.System.now()

        val from = now.minus(alertRule.durationSeconds.value.seconds)

        return repo.getByInterval(
            userId = userId,
            monitoredEndpointId = endpointId,
            from = from,
            to = now
        )
    }
}
