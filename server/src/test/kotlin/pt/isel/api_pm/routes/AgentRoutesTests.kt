package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import pt.isel.api_pm.testutils.registerAndGetToken
import kotlin.test.*

class AgentRoutesTests {

    @Test
    fun `should create agent endpoint`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            val response =
                client.post("/agent/register") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                    {
                        "url":"https://api.github.com",
                        "name":"github-agent"
                    }
                    """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )
        }

    @Test
    fun `should reject agent endpoint without token`() =
        testApplication {

            application { module() }

            val response =
                client.post("/agent/register") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "url":"https://api.github.com",
                            "name":"github-agent"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }
}