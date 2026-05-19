package pt.isel.api_pm.domain.agent

import kotlin.time.Instant

data class Agent (
    val id: UInt,
    val userId: UInt,
    val name: String,
    val token: String,
    val createdAt: Instant,
    val endpoint: AgentEndpoint?
)