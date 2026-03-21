package pt.isel.api_pm.service

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.repo.EndpointRepository

class EndpointService(
    private val repo: EndpointRepository
) {

    suspend fun getAll() = repo.getAll()

    suspend fun add(endpoint: MonitoredEndpoint) = repo.add(endpoint)

    suspend fun delete(id: Int) = repo.delete(id)
}