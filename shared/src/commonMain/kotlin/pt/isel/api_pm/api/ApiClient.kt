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
import io.ktor.client.statement.bodyAsText
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.domain.agent.AgentUiModel
import pt.isel.api_pm.dto.endpoint.StopContinueEndpointRequest
import pt.isel.api_pm.dto.endpoint.UpdateEndpointRequest
import pt.isel.api_pm.dto.metric.AgentAggregatedMetric
import pt.isel.api_pm.dto.metric.AgentRequestMetric

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

            if (response.status.isSuccess()) {

                response.body<String>()

            } else {

                throw Exception(
                    response.bodyAsText()
                )
            }
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
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?,
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
                    intervalSeconds,
                    notification,
                    alertRule
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

            val response = client.post("${ApiConfig.BASE_URL}/auth/login") {

                contentType(ContentType.Application.Json)

                setBody(
                    LoginRequest(
                        username,
                        password
                    )
                )
            }

            if (response.status.isSuccess()) {

                response.body<LoginResponse>().token

            } else {

                throw Exception(
                    "Invalid username or password."
                )
            }
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

    override suspend fun getAgents(
        token: String
    ): Result<List<AgentUiModel>> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/agent") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

            }.body()
        }

    override suspend fun getAgentMetrics(
        token: String,
        agentId: UInt
    ): Result<List<AgentRequestMetric>> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/metrics/agent/$agentId") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

            }.body<List<AgentRequestMetric>>()
        }

    override suspend fun getAgentSummary(
        token: String,
        agentId: UInt
    ): Result<AgentAggregatedMetric> =
        runCatching {

            client.get("${ApiConfig.BASE_URL}/metrics/agent/$agentId/summary") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

            }.body()
        }

    override suspend fun stopEndpoint(
        token: String,
        endpointId: UInt
    ): Result<Unit> =
        runCatching {

            client.post("${ApiConfig.BASE_URL}/endpoints/stop") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(
                    StopContinueEndpointRequest(endpointId)
                )
            }
        }

    override suspend fun continueEndpoint(
        token: String,
        endpointId: UInt
    ): Result<Unit> =
        runCatching {

            client.post("${ApiConfig.BASE_URL}/endpoints/continue") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(
                    StopContinueEndpointRequest(endpointId)
                )
            }
        }

    override suspend fun updateEndpoint(
        token: String,
        request: UpdateEndpointRequest
    ): Result<String> =
        runCatching {

            client.put("${ApiConfig.BASE_URL}/endpoints") {

                header(
                    "Authorization",
                    "Bearer $token"
                )

                contentType(ContentType.Application.Json)

                setBody(request)
            }.body()
        }
}