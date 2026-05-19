package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.agent.AgentEndpoint
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.repo.AgentRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class AgentRepositoryMemory: AgentRepository {
    private val agents = ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, Agent>>()

    override suspend fun register(
        userId: UInt,
        name: String,
        token: String
    ): Agent {
        val idVal = agents[userId]?.keys?.maxOrNull()
        val id = if (idVal == null) 0u else idVal + 1u

        val agent = Agent(
            id = id,
            userId = userId,
            name = name,
            token = token,
            createdAt = Clock.System.now(),
            endpoint = null
        )

        agents.getOrPut(userId) { ConcurrentHashMap() }[id] = agent
        return agent
    }

    override suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        intervalSeconds: IntervalSeconds
    ) {
        val userAgents = agents[userId] ?: throw IllegalArgumentException("User with id $userId not found") // TODO: Mudar as Exceptions
        val agent = userAgents[agentId] ?: throw IllegalArgumentException("Agent with id $agentId not found") // TODO: Mudar as Exceptions

        if (agent.endpoint != null) throw IllegalStateException("Agent with id $agentId already has an endpoint") // TODO: Mudar as Exceptions

        val endpoint = AgentEndpoint(
            name = name,
            intervalSeconds = intervalSeconds,
            createdAt = Clock.System.now(),
        )

        userAgents[agentId] = agent.copy(endpoint = endpoint)
    }
}