package pt.isel.api_pm.domain.endpoint

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.time.Instant

data class MonitoredEndpoint(
    val id: UInt,
    val userId: UInt,
    val url: EndpointUrl,
    val name: String,
    val interval: IntervalSeconds,
    val createdAt: Instant,
    val notification: NotificationConfig,
    val alertRule: AlertRule?,
)