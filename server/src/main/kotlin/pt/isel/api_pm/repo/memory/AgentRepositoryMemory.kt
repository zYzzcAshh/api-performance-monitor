package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.agent.AgentEndpoint
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.repo.AgentRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class AgentRepositoryMemory : AgentRepository {

    private val agents =
        ConcurrentHashMap<UInt, ConcurrentHashMap<UInt, Agent>>()

    override suspend fun register(
        userId: UInt,
        name: String
    ): Agent {

        val idVal =
            agents[userId]?.keys?.maxOrNull()

        val id =
            if (idVal == null) 0u
            else idVal + 1u

        val agent =
            Agent(
                id = id,
                userId = userId,
                name = name,
                createdAt = Clock.System.now(),
                endpoint = null,
                active = false
            )

        agents
            .getOrPut(userId) {
                ConcurrentHashMap()
            }[id] = agent

        return agent
    }

    override suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        method: HttpMethod,
        intervalSeconds: IntervalSeconds
    ) {

        val userAgents =
            agents[userId]
                ?: throw IllegalArgumentException(
                    "User with id $userId not found"
                )

        val agent =
            userAgents[agentId]
                ?: throw IllegalArgumentException(
                    "Agent with id $agentId not found"
                )

        if (agent.endpoint != null) {
            throw IllegalStateException(
                "Agent with id $agentId already has an endpoint"
            )
        }

        val endpoint =
            AgentEndpoint(
                name = name,
                method = method,
                intervalSeconds = intervalSeconds,
                createdAt = Clock.System.now(),
            )

        userAgents[agentId] =
            agent.copy(
                endpoint = endpoint,
                active = true
            )
    }

    override suspend fun inactiveAgent(userId: UInt, agentId: UInt) {
        val userAgents =
            agents[userId]
                ?: throw IllegalArgumentException(
                    "User with id $userId not found"
                )

        val agent =
            userAgents[agentId]
                ?: throw IllegalArgumentException(
                    "Agent with id $agentId not found"
                )

        userAgents[agentId] =
            agent.copy(
                active = false
            )
    }

    override suspend fun getById(
        userId: UInt,
        agentId: UInt
    ): Agent? =
        agents[userId]?.get(agentId)

    override suspend fun getAll(): List<Agent> = agents.values.flatMap { it.values }

    override suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent> {
        return agents.values.flatMap { it.values }.filter { it.endpoint?.intervalSeconds == intervalSeconds }
    }

    override suspend fun getAllActiveByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent> {
        return agents.values.flatMap { it.values }.filter { it.endpoint?.intervalSeconds == intervalSeconds && it.active }
    }
}