package pt.isel.api_pm.service

import pt.isel.api_pm.domain.metric.RequestMetric
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
}
