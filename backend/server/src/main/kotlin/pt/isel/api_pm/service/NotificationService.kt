package pt.isel.api_pm.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.utils.SmtpEmailSender

class NotificationService(
    private val httpClient: HttpClient,
    private val smtpEmailSender: SmtpEmailSender,
) {
    suspend fun notifyAll(notificationConfig: NotificationConfig, endpointName: String) {
        when (notificationConfig) {
            is NotificationConfig.None -> Unit
            is NotificationConfig.Log -> println("Notification: Alert triggered!")
            is NotificationConfig.DiscordWebhook -> sendDiscordWebhook(notificationConfig.webhookUrl, endpointName)
            is NotificationConfig.Email -> sendEmail(notificationConfig.to, notificationConfig.subject)
        }
    }

    private suspend fun sendDiscordWebhook(url: String, endpointName: String) {
        try {
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody("""{"content":"Alert triggered for monitored endpoint $endpointName!"}""")
            }
        } catch (e: Exception) {
            println("Failed to send Discord webhook: ${e.message}")
        }
    }

    private fun sendEmail(to: String, subject: String) {
        smtpEmailSender.send(to, subject, "Alert triggered for monitored endpoint!")
    }
}