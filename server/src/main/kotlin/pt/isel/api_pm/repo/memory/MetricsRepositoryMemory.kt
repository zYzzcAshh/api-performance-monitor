package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Instant

class MetricsRepositoryMemory : MetricsRepository {
    private val metrics = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MutableList<EndpointMetrics>>>()
    private val agentMetrics = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, MutableList<AgentEndpointMetrics>>>()

    override suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: EndpointMetrics,
    ) {
        metrics.getOrPut(userId) { ConcurrentHashMap() }.getOrPut(monitoredEndpointId) { mutableListOf() }.add(metric)
    }

    override suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<EndpointMetrics> = metrics[userId]?.get(monitoredEndpointId)?.toList() ?: emptyList()

    override suspend fun getAll(): List<EndpointMetrics> = metrics.values.flatMap { it.values }.flatten()

    override suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant
    ): List<EndpointMetrics> {
        return metrics[userId]?.get(monitoredEndpointId)?.filter { it.timestamp in from..to } ?: emptyList()
    }

    override suspend fun saveAgentMetrics(
        userId: UInt,
        agentId: UInt,
        message: AgentEndpointMetrics
    ) {
        agentMetrics
            .getOrPut(userId) { ConcurrentHashMap() }
            .getOrPut(agentId) { mutableListOf() }
            .add(message)
    }

    override suspend fun getAllAgentMetrics(): List<AgentEndpointMetrics> = agentMetrics.values.flatMap { it.values }.flatten()

    override suspend fun getAgentMetricsByInterval(
        userId: UInt,
        agentId: UInt,
        from: Instant,
        to: Instant
    ): List<AgentEndpointMetrics> {
        return agentMetrics[userId]?.get(agentId)?.filter { Instant.fromEpochSeconds(it.timestamp) in from..to } ?: emptyList()
    }
}
