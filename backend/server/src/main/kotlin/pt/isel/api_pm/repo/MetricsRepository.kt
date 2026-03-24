package pt.isel.api_pm.repo

import pt.isel.api_pm.dto.metric.RequestMetric

interface MetricsRepository {
    suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: RequestMetric,
    )

    suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<RequestMetric>

    suspend fun getAll(): List<RequestMetric>
}
