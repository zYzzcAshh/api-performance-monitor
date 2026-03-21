package pt.isel.api_pm.service

import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository

class MetricsService(
    private val repo: MetricsRepository
) {

    suspend fun save(metric: RequestMetric) {
        repo.save(metric)
    }

    suspend fun getAll(): List<RequestMetric> {
        return repo.getAll()
    }

    suspend fun getByEndpoint(endpoint: String): List<RequestMetric> {
        return repo.getByEndpoint(endpoint)
    }
}