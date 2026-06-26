package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.IntervalSeconds

@Serializable
data class AgentRegisterRequest (
    val name: String
)