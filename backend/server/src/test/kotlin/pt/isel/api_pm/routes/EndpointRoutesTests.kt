package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import pt.isel.api_pm.app.module
import pt.isel.api_pm.testutils.*
import kotlin.test.*

class EndpointRoutesTests {

    @Test
    fun `should reject request without token`() = testApplication {
        application { module() }

        val response = client.get("/api/endpoints")

        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun `should create endpoint with token`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        val response = createEndpoint(client, token)

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should list endpoints for user`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        createEndpoint(client, token)

        val response = client.get("/api/endpoints") {
            header("Authorization", "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("api.github.com"))
    }

    @Test
    fun `should reject invalid url via API`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        val response = createEndpoint(client, token, url = "not-a-url")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should reject invalid interval via API`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        val response = createEndpoint(client, token, intervalSeconds = 5)

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should reject duplicate endpoint via API`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        createEndpoint(client, token)
        createEndpoint(client, token)

        val response = createEndpoint(client, token)

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `should delete endpoint`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        createEndpoint(client, token)

        val response = client.delete("/api/endpoints/0") {
            header("Authorization", "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}