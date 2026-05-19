package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable

@Serializable
data class AgentRegisterResponse (
    val agentId: UInt,
    val token: String
)