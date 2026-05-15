package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import kotlin.time.Instant

class MetricsRepositoryPostgres : MetricsRepository {
    override suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: RequestMetric,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<RequestMetric> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<RequestMetric> {
        TODO("Not yet implemented")
    }

    override suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant
    ): List<RequestMetric> {
        TODO("Not yet implemented")
    }
}
