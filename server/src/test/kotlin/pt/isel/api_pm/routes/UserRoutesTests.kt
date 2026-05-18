package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import pt.isel.api_pm.testutils.registerAndGetToken
import kotlin.test.*

class UserRoutesTests {

    @Test
    fun `should list users`() =
        testApplication {

            application { module() }

            client.post("/auth/register") {

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "username":"user1",
                        "password":"Password1"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.get("/users")

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertTrue(
                response.bodyAsText()
                    .contains("user1")
            )
        }

    @Test
    fun `should access own user route`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            val response =
                client.get("/users/1") {

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
                    .contains("Successfully accessed")
            )
        }

    @Test
    fun `should reject access without token`() =
        testApplication {

            application { module() }

            val response =
                client.get("/users/1")

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should reject access to another user`() =
        testApplication {

            application { module() }

            val token1 =
                registerAndGetToken(
                    client,
                    "user1"
                )

            registerAndGetToken(
                client,
                "user2"
            )

            val response =
                client.get("/users/2") {

                    header(
                        "Authorization",
                        "Bearer $token1"
                    )
                }

            assertEquals(
                HttpStatusCode.Forbidden,
                response.status
            )
        }

    @Test
    fun `should reject invalid user id`() =
        testApplication {

            application { module() }

            val token =
                registerAndGetToken(
                    client,
                    "user1"
                )

            val response =
                client.get("/users/abc") {

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
    fun `should return empty users list`() =
        testApplication {

            application { module() }

            val response =
                client.get("/users")

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertTrue(
                response.bodyAsText()
                    .contains("[")
            )
        }
}