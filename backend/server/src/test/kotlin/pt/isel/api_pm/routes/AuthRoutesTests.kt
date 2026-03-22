package pt.isel.api_pm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import pt.isel.api_pm.app.module

class AuthRoutesTests {

    @Test
    fun `should register user via API`() = testApplication {
        application { module() }

        val response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user1","password":"Password1"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should login user and return token`() = testApplication {
        application { module() }

        client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user2","password":"Password1"}""")
        }

        val response = client.post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user2","password":"Password1"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("token"))
    }

    @Test
    fun `should reject login with wrong password`() = testApplication {
        application { module() }

        client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"Password1"}""")
        }

        val response = client.post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"wrong"}""")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `should reject invalid password on register`() = testApplication {
        application { module() }

        val response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"abc"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should reject duplicate register`() = testApplication {
        application { module() }

        client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"Password1"}""")
        }

        val response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"user","password":"Password1"}""")
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }
}