package pt.isel.api_pm.service

import io.ktor.client.HttpClient
import pt.isel.api_pm.notification.*
import pt.isel.api_pm.utils.SmtpEmailSender

class NotificationService(
    private val httpClient: HttpClient,
    private val smtpEmailSender: SmtpEmailSender,
) {

    suspend fun notifyAll(
        notificationConfig: NotificationConfig,
        endpointName: String
    ) {

        val sender =
            when (notificationConfig) {

                NotificationConfig.None ->
                    NoOpNotificationSender()

                NotificationConfig.Log ->
                    LogNotificationSender()

                is NotificationConfig.DiscordWebhook ->
                    DiscordNotificationSender(
                        httpClient,
                        notificationConfig.webhookUrl
                    )

                is NotificationConfig.Email ->
                    EmailNotificationSender(
                        smtpEmailSender,
                        notificationConfig.to,
                        notificationConfig.subject
                    )

                is NotificationConfig.SlackWebhook ->
                    SlackNotificationSender(
                        httpClient,
                        notificationConfig.webhookUrl
                    )

                is NotificationConfig.Telegram ->
                    TelegramNotificationSender(
                        httpClient,
                        notificationConfig.botToken,
                        notificationConfig.chatId
                    )

                is NotificationConfig.Webhook ->
                    WebhookNotificationSender(
                        httpClient,
                        notificationConfig.url
                    )
            }

        sender.send(
            endpointName
        )
    }
}