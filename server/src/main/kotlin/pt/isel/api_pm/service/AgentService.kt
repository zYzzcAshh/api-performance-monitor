package pt.isel.api_pm.service

import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.agent.AgentRegisterResponse
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.repo.AgentRepository

class AgentService(
    private val repo: AgentRepository,
    private val jwtService: JwtService
) {
    suspend fun register(userId: UInt, name: String): AgentRegisterResponse {
        // Verificar se já existe um agente com aquele nome associado aquele user

        val agent = repo.register(userId, name)
        val token = jwtService.generateAgentToken(userId, agent.id)
        return AgentRegisterResponse(agent.id, token)
    }

    suspend fun addEndpoint(userId: UInt, agentId: UInt, name: String, intervalSeconds: IntervalSeconds) {
        // Verificar se ja existe um endpoint a ser monitorado por aquele agent
        // Verificar se ja existe um endpoint com aquele nome a ser monitorado

        repo.addEndpoint(userId, agentId, name, intervalSeconds)
    }

    suspend fun getAll() = repo.getAll()
}