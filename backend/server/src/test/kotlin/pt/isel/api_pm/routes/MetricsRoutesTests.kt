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
    fun `should return metrics summary`() = testApplication {
        application { module() }

        val token = registerAndGetToken(client, "user1")

        createEndpoint(client, token)

        client.post("/api/metrics/check") {
            contentType(ContentType.Application.Json)
            setBody("""{"url":"https://api.github.com"}""")
        }

        val response = client.get("/api/metrics/0/summary") {
            header("Authorization", "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("uptime"))
    }
}