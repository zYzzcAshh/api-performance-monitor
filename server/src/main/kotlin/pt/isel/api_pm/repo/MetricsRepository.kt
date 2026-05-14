package pt.isel.api_pm.repo

import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.time.Instant

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

    suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant,
    ) : List<RequestMetric>
}
