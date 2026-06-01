package pt.isel.api_pm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.LoginResponse
import pt.isel.api_pm.dto.user.RegisterRequest

private val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                classDiscriminator = "type"
            }
        )
    }
}

class ApiClient(
    private val client: HttpClient = httpClient
) {

    suspend fun register(
        username: String,
        password: String
    ): Result<String> =
        runCatching {

            val response = client.post("${ApiConfig.BASE_URL}/auth/register") {

                contentType(ContentType.Application.Json)

                setBody(
                    RegisterRequest(
                        username,
                        password
                    )
                )
            }

            response.body()
        }

    suspend fun login(
        username: String,
        password: String
    ): Result<String> =
        runCatching {

            val response: LoginResponse =
                client.post("${ApiConfig.BASE_URL}/auth/login") {

                    contentType(ContentType.Application.Json)

                    setBody(
                        LoginRequest(
                            username,
                            password
                        )
                    )
                }.body()

            response.token
        }

    suspend fun getEndpoints(
        token: String
    ): Result<List<EndpointUiModel>> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/endpoints") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

            }.body()
        }

    suspend fun createEndpointMonitor(
        token: String,
        request: CreateEndpointRequest
    ): Result<String> =
        runCatching {

            client.post("${ApiConfig.BASE_URL}/endpoints") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(request)
            }.body()
        }

    suspend fun getEndpointMetrics(
        token: String,
        endpointId: UInt
    ): Result<String> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/metrics/$endpointId") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)
            }.body()
        }

    suspend fun deleteEndpoint(
        token: String,
        endpointId: UInt
    ): Result<Unit> =
        runCatching {

            client.delete("${ApiConfig.BASE_URL}/endpoints/$endpointId") {

                header(
                    "Authorization",
                    "Bearer $token"
                )
            }
        }
}