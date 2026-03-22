package pt.isel.api_pm.routes

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import pt.isel.api_pm.testutils.getToken
import pt.isel.api_pm.app.module
import kotlin.test.*

class MetricsRoutesTests {

    @Test
    fun `should return metrics summary`() = testApplication {
        application { module() }

        val token = getToken(client, "user1")

        client.post("/api/endpoints/create") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody("""{"url":"https://api.github.com","name":"gh","intervalSeconds":60}""")
        }

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