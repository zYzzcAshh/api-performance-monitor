package pt.isel.api_pm.service

import io.ktor.client.*
import io.ktor.client.request.*
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MonitoringService(
    private val client: HttpClient,
) {
    @OptIn(ExperimentalTime::class)
    suspend fun checkEndpoint(endpoint: MonitoredEndpoint): RequestMetric {
        val start = System.currentTimeMillis()

        val response = when (endpoint.method) {
            HttpMethod.GET -> client.get(endpoint.url.value) {
                headers.append("User-Agent", "Ktor-App")
            }
            HttpMethod.POST -> client.post(endpoint.url.value) {
                headers.append("User-Agent", "Ktor-App")
            }
            HttpMethod.PUT -> client.put(endpoint.url.value) {
                headers.append("User-Agent", "Ktor-App")
            }
            HttpMethod.DELETE -> client.delete(endpoint.url.value) {
                headers.append("User-Agent", "Ktor-App")
            }
        }

        val statusCode = response.status.value

        val latency = System.currentTimeMillis() - start

        return RequestMetric(
            endpoint = endpoint.url,
            timestamp = Clock.System.now(),
            latency = latency,
            statusCode = statusCode,
        )
    }
}
