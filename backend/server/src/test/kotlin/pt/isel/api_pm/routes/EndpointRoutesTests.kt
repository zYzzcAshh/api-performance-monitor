package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import kotlin.test.*

class EndpointRoutesTests {

    private suspend fun getToken(client: io.ktor.client.HttpClient): String {
        client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"Password1"}""")
        }

        val response = client.post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"Password1"}""")
        }

        val body = response.bodyAsText()
        val token = body.substringAfter("token: ").trim()
        return token
    }

    @Test
    fun `should reject request without token`() = testApplication {
        application { module() }

        val response = client.get("/api/endpoints")

        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun `should create endpoint with token`() = testApplication {
        application { module() }

        val token = getToken(client)

        val response = client.post("/api/endpoints/create") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody("""{"url":"https://api.github.com","name":"gh","intervalSeconds":60}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should list endpoints for user`() = testApplication {
        application { module() }

        val token = getToken(client)

        client.post("/api/endpoints/create") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody("""{"url":"https://api.github.com","name":"gh","intervalSeconds":60}""")
        }

        val response = client.get("/api/endpoints") {
            header("Authorization", "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("api.github.com"))
    }
}