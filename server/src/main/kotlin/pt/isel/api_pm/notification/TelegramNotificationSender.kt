package pt.isel.api_pm.notification

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class TelegramNotificationSender(
    private val httpClient: HttpClient,
    private val botToken: String,
    private val chatId: String
) : NotificationSender {

    override suspend fun send(
        endpointName: String
    ) {

        val message =
            NotificationMessageBuilder.build(
                endpointName
            )

        httpClient.post(
            "https://api.telegram.org/bot$botToken/sendMessage"
        ) {

            contentType(
                ContentType.Application.Json
            )

            setBody(
                """
                {
                    "chat_id":"$chatId",
                    "text":"$message"
                }
                """.trimIndent()
            )
        }
    }
}