package pt.isel.api_pm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.dto.agent.AgentCreateEndpointRequest
import pt.isel.api_pm.dto.agent.AgentRegisterRequest
import pt.isel.api_pm.dto.agent.AgentRegisterResponse
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric
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
) : Api {

    override suspend fun register(
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

    override suspend fun agentRegister(
        token: String,
        name: String
    ): Result<String> =
        runCatching {
            val response: AgentRegisterResponse = client.post("${ApiConfig.BASE_URL}/agent/register") {
                contentType(ContentType.Application.Json)
                header(
                    "Authorization",
                    "Bearer $token"
                )
                setBody(
                    AgentRegisterRequest(
                        name
                    )
                )
            }.body()

            response.token
        }

    override suspend fun createAgentEndpoint(
        token: String,
        name: String,
        method: HttpMethod,
        intervalSeconds: Long
    ): Result<String> = runCatching {
        val response = client.post("${ApiConfig.BASE_URL}/agent/endpoints") {
            contentType(ContentType.Application.Json)
            header(
                "Authorization",
                "Bearer $token"
            )
            setBody(
                AgentCreateEndpointRequest(
                    name,
                    method,
                    intervalSeconds
                )
            )
        }

        response.body()
    }

    override suspend fun login(
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

    override suspend fun getEndpoints(
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

    override suspend fun createEndpointMonitor(
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

    override suspend fun getEndpointMetrics(
        token: String,
        endpointId: UInt
    ): Result<List<RequestMetric>> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/metrics/$endpointId") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)
            }.body<List<RequestMetric>>()
        }

    override suspend fun deleteEndpoint(
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

    override suspend fun getMetricsSummary(
        token: String,
        endpointId: UInt
    ): Result<AggregatedMetric> =
        runCatching {

            client.get(
                "${ApiConfig.BASE_URL}/metrics/$endpointId/summary"
            ) {

                header(
                    "Authorization",
                    "Bearer $token"
                )
            }.body()
        }
}