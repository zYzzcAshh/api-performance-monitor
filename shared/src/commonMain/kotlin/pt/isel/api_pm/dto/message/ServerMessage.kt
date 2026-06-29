package pt.isel.api_pm.dto.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerMessage {
    @Serializable
    @SerialName("do_request")
    data class DoRequest(val endpointName: String) : ServerMessage()
}