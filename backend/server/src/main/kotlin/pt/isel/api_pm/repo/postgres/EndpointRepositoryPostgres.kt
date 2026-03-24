package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.repo.EndpointRepository

class EndpointRepositoryPostgres : EndpointRepository {
    override suspend fun getAll(): List<MonitoredEndpoint> {
        TODO("Not yet implemented")
    }

    override suspend fun getByUser(userId: Int): List<MonitoredEndpoint> {
        TODO("Not yet implemented")
    }

    override suspend fun add(
        userId: Int,
        url: String,
        name: String,
        intervalSeconds: Long,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        userId: Int,
        monitoredEndpointId: Int,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun existsByUrlAndUser(
        userId: Int,
        url: String,
    ): Boolean {
        TODO("Not yet implemented")
    }
}
