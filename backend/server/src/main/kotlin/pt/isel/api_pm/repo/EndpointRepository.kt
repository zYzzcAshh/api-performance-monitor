package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint

interface EndpointRepository {

    suspend fun getAll(): List<MonitoredEndpoint>

    suspend fun getByUser(userId: Int): List<MonitoredEndpoint>

    suspend fun add(endpoint: MonitoredEndpoint)

    suspend fun delete(id: Int)
}