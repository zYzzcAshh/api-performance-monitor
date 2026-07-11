package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.notification.NotificationConfig

@Serializable
data class UpdateEndpointRequest(
    val id: UInt,
    val url: String,
    val name: String,
    val method: HttpMethod,
    val intervalSeconds: Long,
    val notification: NotificationConfig,
    val alertRule: AlertRule?
)