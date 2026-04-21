package pt.isel.api_pm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import pt.isel.api_pm.app.BASE_URL
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.LoginResponse
import pt.isel.api_pm.dto.user.RegisterRequest

private val httpClient = HttpClient{
    install(ContentNegotiation) {
        json()
    }
}

class ApiClient(private val client: HttpClient = httpClient) {

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
            val response: LoginResponse = client.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }.body()

            response.token
        }

    suspend fun getEndpoints(token: String): Result<String> =
        runCatching {
            client.get("$BASE_URL/endpoints") {
                header("Authorization", "Bearer $token")
            }.body()
        }

    suspend fun createEndpointMonitor(token: String): Result<String> =
        runCatching {
            client.post("$BASE_URL/endpoints/create") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(CreateEndpointRequest(url = "https://example.com", name = "Example Endpoint", intervalSeconds = 180))
            }.body()
        }

    suspend fun getEndpointMetrics(token: String): Result<String> =
        runCatching {
            client.get("$BASE_URL/metrics/0") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body()
        }
}