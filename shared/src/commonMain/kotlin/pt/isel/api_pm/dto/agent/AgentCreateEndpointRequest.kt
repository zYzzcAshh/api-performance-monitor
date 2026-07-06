package pt.isel.api_pm.dto.agent

import kotlinx.serialization.Serializable
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.notification.NotificationConfig

@Serializable
data class AgentCreateEndpointRequest(
    val name: String,
    val method: HttpMethod,
    val intervalSeconds: Long,
    val notification: NotificationConfig,
    val alertRule: AlertRule?,
)