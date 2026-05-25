package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.endpoint.IntervalSeconds

interface AgentRepository {

    suspend fun register(
        userId: UInt,
        name: String,
        token: String
    ): Agent

    suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        intervalSeconds: IntervalSeconds
    )

    suspend fun getById(
        userId: UInt,
        agentId: UInt
    ): Agent?
}