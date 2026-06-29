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
)

enum class HttpMethod {
    GET, POST, PUT, DELETE
}

fun String.toHttpMethod(): HttpMethod {
    when (this.lowercase()) {
        "POST" -> return HttpMethod.POST
        "PUT" -> return HttpMethod.PUT
        "DELETE" -> return HttpMethod.DELETE
        else -> return HttpMethod.GET
    }
}