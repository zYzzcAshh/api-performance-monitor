package pt.isel.api_pm.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NotificationConfig {

    @Serializable
    @SerialName("none")
    data object None : NotificationConfig()

    @Serializable
    @SerialName("log")
    data object Log : NotificationConfig()

    @Serializable
    @SerialName("discord_webhook")
    data class DiscordWebhook(
        val webhookUrl: String,
    ) : NotificationConfig()

    @Serializable
    @SerialName("email")
    data class Email(
        val to: String,
        val subject: String
    ) : NotificationConfig()
}