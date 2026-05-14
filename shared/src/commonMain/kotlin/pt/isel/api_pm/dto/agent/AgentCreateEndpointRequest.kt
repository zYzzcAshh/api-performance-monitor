package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable

@Serializable
data class AgentCreateEndpointRequest(
    val name: String,
    val intervalSeconds: Long
)