package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import kotlin.test.BeforeTest
import pt.isel.api_pm.app.module
import pt.isel.api_pm.repo.exposed.TestDatabase
import pt.isel.api_pm.testutils.*

class EndpointRoutesTests {

    @BeforeTest
    fun setup() {
        TestDatabase.init()
    }

    @Test
    fun `should reject request without token`() =
        testApplication {

            application { module() }

            val response =
                client.get("/endpoints")

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should create endpoint with token`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "create_user"
                )

            val response =
                createEndpoint(
                    client,
                    token
                )

            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
        }

    @Test
    fun `should list endpoints for user`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "list_user"
                )

            createEndpoint(client, token)

            val response =
                client.get("/endpoints") {

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
                    .contains("api.github.com")
            )
        }

    @Test
    fun `should reject invalid url via API`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "invalid_url_user"
                )

            val response =
                createEndpoint(
                    client,
                    token,
                    url = "not-a-url"
                )

            assertEquals(
                HttpStatusCode.BadRequest,
                response.status
            )
        }

    @Test
    fun `should reject invalid interval via API`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "invalid_interval_user"
                )

            val response =
                createEndpoint(
                    client,
                    token,
                    intervalSeconds = 5
                )

            assertEquals(
                HttpStatusCode.BadRequest,
                response.status
            )
        }

    @Test
    fun `should reject duplicate endpoint via API`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "duplicate_user"
                )

            createEndpoint(client, token)

            val response =
                createEndpoint(client, token)

            assertEquals(
                HttpStatusCode.Conflict,
                response.status
            )
        }

    @Test
    fun `should delete endpoint`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "delete_user"
                )

            createEndpoint(client, token)

            val response =
                client.delete("/endpoints/1") {

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
    fun `should reject endpoint creation without token`() =
        testApplication {

            application { module() }

            val response =
                client.post("/endpoints") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "url":"https://api.github.com",
                            "name":"gh",
                            "intervalSeconds":60
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
    fun `should return empty endpoint list`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "empty_user"
                )

            val response =
                client.get("/endpoints") {

                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertEquals(
                "[]",
                response.bodyAsText()
            )
        }

    @Test
    fun `should reject deleting endpoint without token`() =
        testApplication {

            application { module() }

            val response =
                client.delete("/endpoints/1")

            assertEquals(
                HttpStatusCode.Forbidden,
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
                    "invalid_id_user"
                )

            val response =
                client.delete("/endpoints/abc") {

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
}