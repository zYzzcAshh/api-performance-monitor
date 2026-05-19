package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import pt.isel.api_pm.testutils.*
import kotlin.test.*

class MetricsRoutesTests {

    @Test
    fun `should return metrics summary`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            createEndpoint(
                client,
                token
            )

            client.post("/metrics/check") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "url":"https://api.github.com"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.get("/metrics/1/summary") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertTrue(
                response.bodyAsText()
                    .contains("uptime")
            )
        }

    @Test
    fun `should reject summary without token`() =
        testApplication {

            application { module() }

            val response =
                client.get("/metrics/1/summary")

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should reject metrics list without token`() =
        testApplication {

            application { module() }

            val response =
                client.get("/metrics")

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should reject check without token`() =
        testApplication {

            application { module() }

            val response =
                client.post("/metrics/check") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "url":"https://api.github.com"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should return metrics by endpoint`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            createEndpoint(
                client,
                token
            )

            client.post("/metrics/check") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "url":"https://api.github.com"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.get("/metrics/1") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )
        }

    @Test
    fun `should reject invalid endpoint id`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            val response =
                client.get("/metrics/abc/summary") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

            assertEquals(
                HttpStatusCode.BadRequest,
                response.status
            )
        }

    @Test
    fun `should return empty metrics list`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            val response =
                client.get("/metrics/1") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )
        }
}