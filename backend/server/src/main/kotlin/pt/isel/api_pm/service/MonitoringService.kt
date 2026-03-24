package pt.isel.api_pm.service

import io.ktor.client.*
import io.ktor.client.request.*
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MonitoringService(
    private val client: HttpClient,
) {
    @OptIn(ExperimentalTime::class)
    suspend fun checkEndpoint(url: String): RequestMetric {
        val start = System.currentTimeMillis()

        val response =
            client.get(url) {
                headers.append("User-Agent", "Ktor-App")
            }

        val statusCode = response.status.value

        val latency = System.currentTimeMillis() - start

        return RequestMetric(
            endpoint = url,
            timestamp = Clock.System.now(),
            latency = latency,
            statusCode = statusCode,
        )
    }
}
