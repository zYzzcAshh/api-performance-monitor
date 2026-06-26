package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.dto.AgentMessage
import pt.isel.api_pm.dto.metric.AgentRequestMetric
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Instant

class MetricsRepositoryMemory : MetricsRepository {
    private val metrics = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MutableList<RequestMetric>>>()
    private val agentMetrics = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MutableList<AgentMessage.Metrics>>>()

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

    override suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant
    ): List<RequestMetric> {
        return metrics[userId]?.get(monitoredEndpointId)?.filter { it.timestamp in from..to } ?: emptyList()
    }

    override suspend fun saveAgentMetrics(
        userId: UInt,
        agentId: UInt,
        message: AgentMessage.Metrics
    ) {
        agentMetrics
            .getOrPut(userId) { ConcurrentHashMap() }
            .getOrPut(agentId) { mutableListOf() }
            .add(message)
    }

    override suspend fun getAllAgentMetrics(): List<AgentMessage.Metrics> = agentMetrics.values.flatMap { it.values }.flatten()
}
