package pt.isel.api_pm.service

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class MonitoringServiceTests {

    @Test
    fun `should check endpoint successfully`() =
        runTest {

            val mockEngine =
                MockEngine {

                    respond(
                        content = "OK",
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            HttpHeaders.ContentType,
                            "text/plain"
                        )
                    )
                }

            val client =
                HttpClient(mockEngine) {
                    install(ContentNegotiation) {
                        json()
                    }
                }

            val service =
                MonitoringService(client)

            val metric =
                service.checkEndpoint(
                    MonitoredEndpoint(
                        id = 1u,
                        userId = 1u,
                        name = "Monitored endpoint",
                        url = EndpointUrl("https://api.github.com"),
                        method = HttpMethod.GET,
                        interval = IntervalSeconds(60),
                        createdAt = Clock.System.now(),
                        notification = NotificationConfig.None,
                        alertRule = null,
                        active = true
                    )
                )

            assertEquals(
                200,
                metric.statusCode
            )

            assertEquals(
                "https://api.github.com",
                metric.endpoint.value
            )

            assertTrue(
                metric.latency >= 0
            )
        }

    @Test
    fun `should return 500 status code`() =
        runTest {

            val mockEngine =
                MockEngine {

                    respond(
                        content = "ERROR",
                        status = HttpStatusCode.InternalServerError
                    )
                }

            val client =
                HttpClient(mockEngine)

            val service =
                MonitoringService(client)

            val metric =
                service.checkEndpoint(
                    MonitoredEndpoint(
                        id = 1u,
                        userId = 1u,
                        name = "Monitored endpoint",
                        url = EndpointUrl("https://api.github.com"),
                        method = HttpMethod.GET,
                        interval = IntervalSeconds(60),
                        createdAt = Clock.System.now(),
                        notification = NotificationConfig.None,
                        alertRule = null,
                        active = true
                    )
                )

            assertEquals(
                500,
                metric.statusCode
            )
        }
}