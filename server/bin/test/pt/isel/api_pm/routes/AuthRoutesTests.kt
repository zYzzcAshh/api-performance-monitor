package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import kotlin.test.*

class AuthRoutesTests {

    @Test
    fun `should register user via API`() =
        testApplication {

            application {
                module()
            }

            val response =
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

            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
        }

    @Test
    fun `should login user and return token`() =
        testApplication {

            application {
                module()
            }

            client.post("/auth/register") {

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "username":"user2",
                        "password":"Password1"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.post("/auth/login") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"user2",
                            "password":"Password1"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertTrue(
                response.bodyAsText().contains("token")
            )
        }

    @Test
    fun `should reject login with wrong password`() =
        testApplication {

            application {
                module()
            }

            client.post("/auth/register") {

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "username":"user",
                        "password":"Password1"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.post("/auth/login") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"user",
                            "password":"WrongPassword1"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.Unauthorized,
                response.status
            )
        }

    @Test
    fun `should reject invalid password on register`() =
        testApplication {

            application {
                module()
            }

            val response =
                client.post("/auth/register") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"user",
                            "password":"abc"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.BadRequest,
                response.status
            )
        }

    @Test
    fun `should reject duplicate register`() =
        testApplication {

            application {
                module()
            }

            client.post("/auth/register") {

                contentType(ContentType.Application.Json)

                setBody(
                    """
                    {
                        "username":"user",
                        "password":"Password1"
                    }
                    """.trimIndent()
                )
            }

            val response =
                client.post("/auth/register") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"user",
                            "password":"Password1"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.Conflict,
                response.status
            )
        }

    @Test
    fun `should reject login with non existing user`() =
        testApplication {

            application {
                module()
            }

            val response =
                client.post("/auth/login") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"ghost",
                            "password":"Password1"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.Unauthorized,
                response.status
            )
        }

    @Test
    fun `should reject empty username`() =
        testApplication {

            application {
                module()
            }

            val response =
                client.post("/auth/register") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        """
                        {
                            "username":"",
                            "password":"Password1"
                        }
                        """.trimIndent()
                    )
                }

            assertEquals(
                HttpStatusCode.BadRequest,
                response.status
            )
        }
}