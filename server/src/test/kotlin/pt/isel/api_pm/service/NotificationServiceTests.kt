package pt.isel.api_pm.service

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.utils.SmtpEmailSender
import kotlin.test.Test

class NotificationServiceTests {

    private val mockEngine =
        MockEngine {

            respond(
                content = "OK",
                status = HttpStatusCode.OK
            )
        }

    private val client =
        HttpClient(mockEngine)

    private val smtpSender =
        SmtpEmailSender(
            email = "test@test.com",
            password = "password"
        )

    private val service =
        NotificationService(
            client,
            smtpSender
        )

    @Test
    fun `should handle none notification`() =
        runTest {

            service.notifyAll(
                NotificationConfig.None,
                "github-api"
            )
        }

    @Test
    fun `should handle log notification`() =
        runTest {

            service.notifyAll(
                NotificationConfig.Log,
                "github-api"
            )
        }

    @Test
    fun `should send discord webhook`() =
        runTest {

            service.notifyAll(
                NotificationConfig.DiscordWebhook(
                    webhookUrl = "https://discord.com/api/webhooks/test"
                ),
                "github-api"
            )
        }

    @Test
    fun `should send slack webhook`() =
        runTest {

            service.notifyAll(
                NotificationConfig.SlackWebhook(
                    webhookUrl = "https://hooks.slack.com/test"
                ),
                "github-api"
            )
        }
}