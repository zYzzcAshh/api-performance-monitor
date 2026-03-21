package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository

class MetricsRepositoryPostgres : MetricsRepository {

    override suspend fun save(metric: RequestMetric) {
        TODO("Not yet implemented")
    }

    override suspend fun getByEndpoint(endpoint: String): List<RequestMetric> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<RequestMetric> {
        TODO("Not yet implemented")
    }
}