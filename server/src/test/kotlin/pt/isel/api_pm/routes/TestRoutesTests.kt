package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRoutesTests {

    @Test
    fun `should return ok`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/ok")

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertEquals(
                "OK",
                response.bodyAsText()
            )
        }

    @Test
    fun `should return internal server error`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/error")

            assertEquals(
                HttpStatusCode.InternalServerError,
                response.status
            )
        }

    @Test
    fun `should return not found`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/notfound")

            assertEquals(
                HttpStatusCode.NotFound,
                response.status
            )
        }

    @Test
    fun `should return slow response`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/slow")

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )

            assertTrue(
                response.bodyAsText()
                    .contains("Slow")
            )
        }

    @Test
    fun `should return random response`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/random")

            assertTrue(
                response.status in listOf(
                    HttpStatusCode.OK,
                    HttpStatusCode.BadRequest,
                    HttpStatusCode.ServiceUnavailable
                )
            )
        }

    @Test
    fun `should proxy github request`() =
        testApplication {

            application { module() }

            val response =
                client.get("/test/github")

            assertEquals(
                HttpStatusCode.OK,
                response.status
            )
        }
}