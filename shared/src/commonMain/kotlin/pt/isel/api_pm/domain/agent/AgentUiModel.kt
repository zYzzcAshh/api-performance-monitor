package pt.isel.api_pm.domain.agent

import kotlinx.serialization.Serializable

@Serializable
data class AgentUiModel(
    val id: UInt,
    val name: String,
    val active: Boolean,
    val createdAt: String,
    val endpointName: String?
)