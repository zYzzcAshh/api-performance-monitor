package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.time.Instant

interface MetricsRepository {
    suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: EndpointMetrics,
    )

    suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<EndpointMetrics>

    suspend fun getAll(): List<EndpointMetrics>

    suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant,
    ) : List<EndpointMetrics>

    suspend fun saveAgentMetrics(
        userId: UInt,
        agentId: UInt,
        message: AgentEndpointMetrics,
    )

    suspend fun getAllAgentMetrics() : List<AgentEndpointMetrics>

    suspend fun getAgentMetricsByInterval(
        userId: UInt,
        agentId: UInt,
        from: Instant,
        to: Instant,
    ) : List<AgentEndpointMetrics>
}
