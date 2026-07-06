package pt.isel.api_pm.domain.agent

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.time.Instant

data class AgentEndpoint (
    val name: String,
    val method: HttpMethod,
    val intervalSeconds: IntervalSeconds,
    val createdAt: Instant,
    val notification: NotificationConfig,
    val alertRule: AlertRule?
)