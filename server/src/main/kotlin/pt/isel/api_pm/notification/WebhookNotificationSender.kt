package pt.isel.api_pm.notification

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class WebhookNotificationSender(
    private val httpClient: HttpClient,
    private val webhookUrl: String
) : NotificationSender {

    override suspend fun send(
        endpointName: String
    ) {

        val message =
            NotificationMessageBuilder.build(
                endpointName
            )

        httpClient.post(webhookUrl) {

            contentType(
                ContentType.Application.Json
            )

            setBody(
                """
                {
                    "message":"$message"
                }
                """.trimIndent()
            )
        }
    }
}