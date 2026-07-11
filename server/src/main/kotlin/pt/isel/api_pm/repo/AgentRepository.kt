package pt.isel.api_pm.repo

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.notification.NotificationConfig

interface AgentRepository {

    suspend fun register(
        userId: UInt,
        name: String
    ): Agent

    suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        method: HttpMethod,
        intervalSeconds: IntervalSeconds,
        notification: NotificationConfig,
        alertRule: AlertRule?
    )

    suspend fun inactiveAgent(userId: UInt, agentId: UInt)

    suspend fun activeAgent(userId: UInt, agentId: UInt)

    suspend fun getById(
        userId: UInt,
        agentId: UInt
    ): Agent?

    suspend fun getAll(): List<Agent>

    suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent>

    suspend fun getAllActiveByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent>
}