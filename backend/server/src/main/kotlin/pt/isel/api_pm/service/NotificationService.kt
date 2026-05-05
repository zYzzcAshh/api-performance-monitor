package pt.isel.api_pm.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pt.isel.api_pm.notification.NotificationConfig

class NotificationService(
    private val httpClient: HttpClient,
) {
    suspend fun notifyAll(notificationConfig: NotificationConfig) {
        when (notificationConfig) {
            is NotificationConfig.None -> Unit
            is NotificationConfig.Log -> println("Notification: Alert triggered!")
            is NotificationConfig.DiscordWebhook -> sendDiscordWebhook(notificationConfig.webhookUrl, httpClient)
        }
    }

    private suspend fun sendDiscordWebhook(url: String, client: HttpClient) {
        try {
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody("""{"content":"Alert triggered for monitored endpoint!"}""")
            }
        } catch (e: Exception) {
            println("Failed to send Discord webhook: ${e.message}")
        }
    }
}