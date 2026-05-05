package pt.isel.api_pm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.api_pm.alert.AggregationType
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.alert.ComparisonOperator
import pt.isel.api_pm.app.BASE_URL
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.LoginResponse
import pt.isel.api_pm.dto.user.RegisterRequest
import pt.isel.api_pm.notification.NotificationConfig

private val httpClient = HttpClient{
    install(ContentNegotiation) {
        json()
    }
}

class ApiClient(private val client: HttpClient = httpClient) {
    val notificationConfig = NotificationConfig.DiscordWebhook(
        webhookUrl = "https://discord.com/api/webhooks/1501258756464316447/QwewEWmGGk830Pfy_NJz7XUsMMS8miqMowaUrtfC7SRpSWDyQ1c-Z8IUDmb6IkYhwd6V"
    )
    val alertRule = ExampleAlerts.a5

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

    suspend fun createEndpointMonitor(token: String, name: String, url: String, intervalSeconds: Int): Result<String> =
        runCatching {
            val request = CreateEndpointRequest(url = url, name = name, intervalSeconds = intervalSeconds.toLong(), notification = notificationConfig, alertRule = alertRule)
            println(Json.encodeToString(request))

            client.post("$BASE_URL/endpoints/create") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(request)
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

object ExampleAlerts {
    val a1 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 1,
        durationSeconds = 60,
        aggregation = AggregationType.COUNT(1),
    ) // If atleast 1 request returned status code bigger than or equal to 1 in the last 60 seconds, trigger an alert

    val a2 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 100,
        durationSeconds = 60,
        aggregation = AggregationType.COUNT(1),
    ) // If atleast 1 request returned status code bigger than or equal to 100 in the last 60 seconds, trigger an alert

    val a3 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 500,
        durationSeconds = 60,
        aggregation = AggregationType.COUNT(1),
    ) // If atleast 1 request returned status code bigger than or equal to 500 in the last 60 seconds, trigger an alert

    val a4 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 100,
        durationSeconds = 60,
        aggregation = AggregationType.COUNT(5),
    ) // If atleast 5 requests returned status code bigger than or equal to 100 in the last 60 seconds, trigger an alert

    val a5 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 100,
        durationSeconds = 60,
        aggregation = AggregationType.ALL
    ) // If all requests returned status code bigger than or equal to 100 in the last 60 seconds, trigger an alert

    val a6 = AlertRule.StatusCodeRule(
        operator = ComparisonOperator.GTE,
        value = 150,
        durationSeconds = 60,
        aggregation = AggregationType.AVG
    ) // If the average status code of all requests in the last 60 seconds is bigger than or equal to 150, trigger an alert
}