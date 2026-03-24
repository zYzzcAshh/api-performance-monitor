package pt.isel.api_pm.repo

import pt.isel.api_pm.dto.metric.RequestMetric

interface MetricsRepository {
    suspend fun save(
        userId: Int,
        monitoredEndpointId: Int,
        metric: RequestMetric,
    )

    suspend fun getByEndpoint(
        userId: Int,
        monitoredEndpointId: Int,
    ): List<RequestMetric>

    suspend fun getAll(): List<RequestMetric>
}
