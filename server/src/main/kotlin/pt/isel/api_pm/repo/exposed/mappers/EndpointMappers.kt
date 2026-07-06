package pt.isel.api_pm.repo.exposed.mappers

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.ResultRow
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig

val json = Json {
    ignoreUnknownKeys = true
}

fun ResultRow.toEndpoint(): MonitoredEndpoint =
    MonitoredEndpoint(
        id = this[MonitoredEndpointTable.id].toUInt(),
        userId = this[MonitoredEndpointTable.userId].toUInt(),
        url = EndpointUrl(this[MonitoredEndpointTable.url]),
        name = this[MonitoredEndpointTable.name],
        method = this[MonitoredEndpointTable.method],
        interval = IntervalSeconds(this[MonitoredEndpointTable.intervalSeconds]),
        createdAt = this[MonitoredEndpointTable.createdAt],
        notification = this.toNotification(),
        alertRule = this.toAlertRule(),
        active = this[MonitoredEndpointTable.active],
    )

private fun ResultRow.toNotification(): NotificationConfig {
    val type = this[MonitoredEndpointTable.notificationType]
    val data = this[MonitoredEndpointTable.notificationData]

    return when (type) {
        "none" ->
            NotificationConfig.None

        "log" ->
            NotificationConfig.Log

        "discord_webhook" -> {
            val jsonData = data ?: return NotificationConfig.None
            json.decodeFromString<NotificationConfig.DiscordWebhook>(jsonData)
        }

        "email" -> {
            val jsonData = data ?: return NotificationConfig.None
            json.decodeFromString<NotificationConfig.Email>(jsonData)
        }

        "slack_webhook" -> {
            val jsonData = data ?: return NotificationConfig.None
            json.decodeFromString<NotificationConfig.SlackWebhook>(jsonData)
        }

        "telegram" -> {
            val jsonData = data ?: return NotificationConfig.None
            json.decodeFromString<NotificationConfig.Telegram>(jsonData)
        }

        "webhook" -> {
            val jsonData = data ?: return NotificationConfig.None
            json.decodeFromString<NotificationConfig.Webhook>(jsonData)
        }

        else ->
            NotificationConfig.None
    }
}

private fun ResultRow.toAlertRule(): AlertRule? {
    val type = this[MonitoredEndpointTable.alertRuleType] ?: return null
    val data = this[MonitoredEndpointTable.alertRuleData] ?: return null

    return when (type) {
        "status_code" ->
            json.decodeFromString<AlertRule.StatusCodeRule>(data)

        "latency" ->
            json.decodeFromString<AlertRule.LatencyRule>(data)

        "down_time" ->
            json.decodeFromString<AlertRule.DownTimeRule>(data)

        else -> null
    }
}