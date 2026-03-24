package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository

class MetricsRepositoryPostgres : MetricsRepository {
    override suspend fun save(
        userId: Int,
        monitoredEndpointId: Int,
        metric: RequestMetric,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getByEndpoint(
        userId: Int,
        monitoredEndpointId: Int,
    ): List<RequestMetric> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<RequestMetric> {
        TODO("Not yet implemented")
    }
}
