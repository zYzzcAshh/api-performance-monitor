package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.agent.Agent

@Serializable
data class AgentDTO(
    val id: UInt,
    val name: String,
    val active: Boolean,
    val createdAt: String,
    val endpointName: String?
)

fun Agent.toDTO() =
    AgentDTO(
        id = id,
        name = name,
        active = active,
        createdAt = createdAt.toString(),
        endpointName = endpoint?.name
    )

fun List<Agent>.toDTO() =
    map { it.toDTO() }