package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import java.util.concurrent.ConcurrentHashMap

class MetricsRepositoryMemory : MetricsRepository {
    private val metrics = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MutableList<RequestMetric>>>()

    override suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: RequestMetric,
    ) {
        metrics.getOrPut(userId) { ConcurrentHashMap() }.getOrPut(monitoredEndpointId) { mutableListOf() }.add(metric)
    }

    override suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<RequestMetric> = metrics[userId]?.get(monitoredEndpointId)?.toList() ?: emptyList()

    override suspend fun getAll(): List<RequestMetric> = metrics.values.flatMap { it.values }.flatten()
}
