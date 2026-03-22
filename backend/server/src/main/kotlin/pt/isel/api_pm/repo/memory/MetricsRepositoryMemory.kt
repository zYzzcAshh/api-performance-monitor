package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import java.util.concurrent.ConcurrentHashMap

class MetricsRepositoryMemory : MetricsRepository {
    private val metrics = ConcurrentHashMap<Int, ConcurrentHashMap<Int, MutableList<RequestMetric>>>()
    // first Int is the UserId, second Int is the MonitoredEndpointId

    override suspend fun save(
        userId: Int,
        monitoredEndpointId: Int,
        metric: RequestMetric,
    ) {
        metrics.getOrPut(userId) { ConcurrentHashMap() }.getOrPut(monitoredEndpointId) { mutableListOf() }.add(metric)
    }

    override suspend fun getByEndpoint(
        userId: Int,
        monitoredEndpointId: Int,
    ): List<RequestMetric> = metrics[userId]?.get(monitoredEndpointId)?.toList() ?: emptyList()

    override suspend fun getAll(): List<RequestMetric> = metrics.values.flatMap { it.values }.flatten()
}
