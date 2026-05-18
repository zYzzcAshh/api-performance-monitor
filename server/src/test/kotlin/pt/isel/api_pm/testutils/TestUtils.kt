package pt.isel.api_pm.testutils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun registerAndGetToken(
    client: HttpClient,
    username: String = "user",
): String {

    client.post("/auth/register") {

        contentType(ContentType.Application.Json)

        setBody(
            """
            {
                "username":"$username",
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
                    "username":"$username",
                    "password":"Password1"
                }
                """.trimIndent()
            )
        }

    val body =
        response.bodyAsText()

    return body
        .substringAfter("\"token\":\"")
        .substringBefore("\"")
}

suspend fun createEndpoint(
    client: HttpClient,
    token: String,
    url: String = "https://api.github.com",
    name: String = "gh",
    intervalSeconds: Long = 60,
): HttpResponse =

    client.post("/endpoints") {

        header(
            "Authorization",
            "Bearer $token"
        )

        contentType(ContentType.Application.Json)

        setBody(
            """
            {
                "url":"$url",
                "name":"$name",
                "intervalSeconds":$intervalSeconds
            }
            """.trimIndent()
        )
    }