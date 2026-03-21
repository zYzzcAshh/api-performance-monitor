package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository

class MetricsRepositoryMemory : MetricsRepository {

    private val metrics = mutableListOf<RequestMetric>()

    override suspend fun save(metric: RequestMetric) {
        metrics.add(metric)
    }

    override suspend fun getByEndpoint(endpoint: String): List<RequestMetric> {
        return metrics.filter { it.endpoint == endpoint }
    }

    override suspend fun getAll(): List<RequestMetric> {
        return metrics
    }
}