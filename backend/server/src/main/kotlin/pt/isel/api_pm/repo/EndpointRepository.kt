package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint

interface EndpointRepository {
    suspend fun getAll(): List<MonitoredEndpoint>

    suspend fun getByUser(userId: Int): List<MonitoredEndpoint>

    suspend fun add(
        userId: Int,
        url: String,
        name: String,
        intervalSeconds: Long,
    )

    suspend fun delete(
        userId: Int,
        monitoredEndpointId: Int,
    )

    suspend fun existsByUrlAndUser(
        userId: Int,
        url: String,
    ): Boolean
}
