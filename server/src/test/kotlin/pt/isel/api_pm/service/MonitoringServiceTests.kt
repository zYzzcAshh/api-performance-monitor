package pt.isel.api_pm.service

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
                    EndpointUrl("https://api.github.com")
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
                    EndpointUrl("https://api.github.com")
                )

            assertEquals(
                500,
                metric.statusCode
            )
        }
}