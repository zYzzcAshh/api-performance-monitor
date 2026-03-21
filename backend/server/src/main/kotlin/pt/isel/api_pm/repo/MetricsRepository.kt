package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.metric.RequestMetric

interface MetricsRepository {

    suspend fun save(metric: RequestMetric)

    suspend fun getByEndpoint(endpoint: String): List<RequestMetric>

    suspend fun getAll(): List<RequestMetric>
}