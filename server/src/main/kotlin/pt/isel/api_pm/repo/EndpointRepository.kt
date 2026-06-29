package pt.isel.api_pm.repo

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig

interface EndpointRepository {
    suspend fun getAll(): List<MonitoredEndpoint>

    suspend fun getByUser(userId: UInt): List<MonitoredEndpoint>

    suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        method: HttpMethod,
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?
    )

    suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    )

    suspend fun existsByUrlAndUser(
        userId: UInt,
        url: String,
    ): Boolean
}
