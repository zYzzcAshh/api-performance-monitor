package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.notification.NotificationConfig

@Serializable
data class CreateEndpointRequest(
    val url: String,
    val name: String,
    val intervalSeconds: Long,
    val notification: NotificationConfig = NotificationConfig.None,
    val alertRule: AlertRule? = null,
)