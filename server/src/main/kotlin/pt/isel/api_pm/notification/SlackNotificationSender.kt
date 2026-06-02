package pt.isel.api_pm.notification

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.slf4j.LoggerFactory

class SlackNotificationSender(
    private val httpClient: HttpClient,
    private val webhookUrl: String,
) : NotificationSender {

    private val logger =
        LoggerFactory.getLogger(
            SlackNotificationSender::class.java
        )

    override suspend fun send(
        endpointName: String
    ) {

        try {

            httpClient.post(webhookUrl) {

                contentType(
                    ContentType.Application.Json
                )

                val message =
                    NotificationMessageBuilder.build(
                        endpointName
                    )

                setBody(
                    """{"text":"$message"}"""
                )
            }

        } catch (e: Exception) {

            logger.warn(
                "Failed to send Slack webhook: ${e.message}"
            )
        }
    }
}