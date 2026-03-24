package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint

interface EndpointRepository {
    suspend fun getAll(): List<MonitoredEndpoint>

    suspend fun getByUser(userId: UInt): List<MonitoredEndpoint>

    suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        intervalSeconds: Long,
    )

    suspend fun delete(userId: UInt, monitoredEndpointId: UInt)

    suspend fun existsByUrlAndUser(userId: UInt, url: String): Boolean
}