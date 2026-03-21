package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.repo.EndpointRepository

class EndpointRepositoryMemory : EndpointRepository {

    private val endpoints = mutableListOf<MonitoredEndpoint>()

    override suspend fun getAll(): List<MonitoredEndpoint> = endpoints

    override suspend fun getByUser(userId: Int): List<MonitoredEndpoint> {
        return endpoints.filter { it.userId == userId }
    }

    override suspend fun add(endpoint: MonitoredEndpoint) {
        endpoints.add(endpoint)
    }

    override suspend fun delete(id: Int) {
        endpoints.removeIf { it.id == id }
    }
}