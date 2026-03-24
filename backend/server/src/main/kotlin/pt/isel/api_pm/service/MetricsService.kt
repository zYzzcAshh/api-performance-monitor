package pt.isel.api_pm.service

import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.dto.MetricsSummaryDTO
import pt.isel.api_pm.repo.MetricsRepository

class MetricsService(
    private val repo: MetricsRepository,
) {
    suspend fun save(
        userId: Int,
        monitoredEndpointId: Int,
        metric: RequestMetric,
    ) = repo.save(userId, monitoredEndpointId, metric)

    suspend fun getAll(): List<RequestMetric> = repo.getAll()

    suspend fun getByEndpoint(
        userId: Int,
        monitoredEndpointId: Int,
    ): List<RequestMetric> = repo.getByEndpoint(userId, monitoredEndpointId)

    suspend fun getSummary(
        userId: Int,
        endpointId: Int,
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
}
