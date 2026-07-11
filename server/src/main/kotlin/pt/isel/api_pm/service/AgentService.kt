package pt.isel.api_pm.service

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.agent.AgentRegisterResponse
import pt.isel.api_pm.exceptions.AgentRegistrationFailedException
import pt.isel.api_pm.exceptions.EndpointAlreadyExistsException
import pt.isel.api_pm.exceptions.EndpointNameAlreadyInUseException
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.AgentRepository

class AgentService(
    private val repo: AgentRepository,
    private val jwtService: JwtService
) {
    suspend fun register(userId: UInt, name: String): AgentRegisterResponse {
        val existing = repo.getAll().find { it.userId == userId && it.name == name }
        if (existing != null) throw AgentRegistrationFailedException(name)

        val agent = repo.register(userId, name)
        val token = jwtService.generateAgentToken(userId, agent.id)
        return AgentRegisterResponse(agent.id, token)
    }

    suspend fun getByIds(userId: UInt, agentId: UInt): Agent? {
        return repo.getById(userId, agentId)
    }

    suspend fun addEndpoint(userId: UInt, agentId: UInt, name: String, method: HttpMethod, intervalSeconds: IntervalSeconds, notification: NotificationConfig, alertRule: AlertRule?) {
        val existing = repo.getAll().find { it.userId == userId && it.id == agentId && it.endpoint?.name == name }
        if (existing != null) throw EndpointAlreadyExistsException(name)
        val existingName = repo.getAll().find { it.userId == userId && it.id == agentId && it.endpoint?.name == name }?.name
        if (existingName != null) throw EndpointNameAlreadyInUseException(existingName)

        repo.addEndpoint(userId, agentId, name, method, intervalSeconds, notification, alertRule)
    }

    suspend fun inactiveAgent(userId: UInt, agentId: UInt) {
        repo.inactiveAgent(userId, agentId)
    }

    suspend fun activeAgent(userId: UInt, agentId: UInt) {
        repo.activeAgent(userId, agentId)
    }

    suspend fun getAll() = repo.getAll()

    suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds) = repo.getAllByIntervalSeconds(intervalSeconds)

    suspend fun getAllActiveByIntervalSeconds(intervalSeconds: IntervalSeconds) = repo.getAllActiveByIntervalSeconds(intervalSeconds)
}