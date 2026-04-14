package pt.isel.api_pm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import pt.isel.api_pm.BASE_URL
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.RegisterRequest

class ApiClient(private val client: HttpClient) {

    suspend fun register(username: String, password: String): Result<String> =
        runCatching {
            val response = client.post("$BASE_URL/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, password))
            }
            response.body()
        }

    suspend fun login(username: String, password: String): Result<String> =
        runCatching {
            val response = client.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            response.body()
        }

    suspend fun getEndpoints(token: String): Result<String> =
        runCatching {
            client.get("$BASE_URL/endpoints") {
                header("Authorization", "Bearer $token")
            }.body()
        }
}