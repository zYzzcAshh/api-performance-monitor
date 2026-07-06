package pt.isel.api_pm.domain.endpoint

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.time.Instant

data class MonitoredEndpoint(
    val id: UInt,
    val userId: UInt,
    val url: EndpointUrl,
    val name: String,
    val method: HttpMethod,
    val interval: IntervalSeconds,
    val createdAt: Instant,
    val notification: NotificationConfig,
    val alertRule: AlertRule?,
    val active: Boolean
)

enum class HttpMethod {
    GET, POST, PUT, DELETE
}

fun String.toHttpMethod(): HttpMethod {
    return when (this.lowercase()) {
        "POST" -> HttpMethod.POST
        "PUT" -> HttpMethod.PUT
        "DELETE" -> HttpMethod.DELETE
        else -> HttpMethod.GET
    }
}