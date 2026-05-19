package pt.isel.api_pm.domain.agent

import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import kotlin.time.Instant

data class AgentEndpoint (
    val name: String,
    val intervalSeconds: IntervalSeconds,
    val createdAt: Instant,
)