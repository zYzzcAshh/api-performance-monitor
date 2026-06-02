package pt.isel.api_pm.repo.exposed.serializers

import kotlinx.serialization.json.Json
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.notification.NotificationConfig

private val json = Json {
    ignoreUnknownKeys = true
}

fun NotificationConfig.toDb(): Pair<String, String?> {

    return when (this) {

        NotificationConfig.None ->
            "none" to null

        NotificationConfig.Log ->
            "log" to null

        is NotificationConfig.DiscordWebhook ->
            "discord_webhook" to json.encodeToString(this)

        is NotificationConfig.Email ->
            "email" to json.encodeToString(this)

        is NotificationConfig.SlackWebhook ->
            "slack_webhook" to json.encodeToString(this)
    }
}

fun AlertRule.toDb(): Pair<String, String> {

    return when (this) {

        is AlertRule.StatusCodeRule ->
            "status_code" to json.encodeToString(this)

        is AlertRule.LatencyRule ->
            "latency" to json.encodeToString(this)

        is AlertRule.DownTimeRule ->
            "down_time" to json.encodeToString(this)
    }
}