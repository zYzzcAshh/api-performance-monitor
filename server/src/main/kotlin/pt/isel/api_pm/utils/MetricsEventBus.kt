package pt.isel.api_pm.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics

data class EndpointMetricEvent(
    val userId: UInt,
    val endpointId: UInt,
    val metric: EndpointMetrics
)

data class AgentMetricEvent(
    val userId: UInt,
    val agentId: UInt,
    val metric: AgentEndpointMetrics
)

class MetricsEventBus {
    private val endpointEvents = MutableSharedFlow<EndpointMetricEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )

    private val agentEvents = MutableSharedFlow<AgentMetricEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )

    suspend fun publishEndpoint(userId: UInt, endpointId: UInt, metric: EndpointMetrics) {
        endpointEvents.emit(EndpointMetricEvent(userId, endpointId, metric))
    }

    suspend fun publishAgent(userId: UInt, agentId: UInt, metric: AgentEndpointMetrics) {
        agentEvents.emit(AgentMetricEvent(userId, agentId, metric))
    }

    fun subscribeEndpoint(userId: UInt, endpointId: UInt): Flow<EndpointMetrics> {
        return endpointEvents
            .filter { it.userId == userId && it.endpointId == endpointId }
            .map { it.metric }
    }

    fun subscribeAgent(userId: UInt, agentId: UInt): Flow<AgentEndpointMetrics> {
        return agentEvents
            .filter { it.userId == userId && it.agentId == agentId }
            .map { it.metric }
    }
}