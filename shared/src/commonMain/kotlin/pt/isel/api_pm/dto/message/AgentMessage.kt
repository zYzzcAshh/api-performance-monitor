package pt.isel.api_pm.dto.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
sealed class AgentMessage {
    @Serializable
    @SerialName("metrics")
    data class Metrics(
        val endpointName: String,
        val statusCode: Int,
        val latency: Long,
        val timestamp: Instant,
    ) : AgentMessage()

    @Serializable
    @SerialName("error")
    data class Error(val endpointName: String, val message: String) : AgentMessage()
}