package pt.isel.api_pm.testutils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getToken(client: HttpClient, username: String = "user"): String {
    client.post("/api/auth/register") {
        contentType(ContentType.Application.Json)
        setBody("""{"username":"$username","password":"Password1"}""")
    }

    val response = client.post("/api/auth/login") {
        contentType(ContentType.Application.Json)
        setBody("""{"username":"$username","password":"Password1"}""")
    }

    return response.bodyAsText().substringAfter("token: ").trim()
}