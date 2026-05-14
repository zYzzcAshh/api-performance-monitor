package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.EndpointRepository

class EndpointRepositoryPostgres : EndpointRepository {
    override suspend fun getAll(): List<MonitoredEndpoint> {
        TODO("Not yet implemented")
    }

    override suspend fun getByUser(userId: UInt): List<MonitoredEndpoint> {
        TODO("Not yet implemented")
    }

    override suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun existsByUrlAndUser(
        userId: UInt,
        url: String,
    ): Boolean {
        TODO("Not yet implemented")
    }
}
