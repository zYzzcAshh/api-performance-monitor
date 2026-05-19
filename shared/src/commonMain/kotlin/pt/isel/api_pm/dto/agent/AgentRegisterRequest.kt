package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable

@Serializable
data class AgentRegisterRequest (
    val name: String
)