package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.HttpMethod

@Serializable
data class AgentCreateEndpointRequest(
    val name: String,
    val method: HttpMethod,
    val intervalSeconds: Long
)