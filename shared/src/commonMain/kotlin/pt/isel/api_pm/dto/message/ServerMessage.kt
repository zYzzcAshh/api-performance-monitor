package pt.isel.api_pm.dto.message

import kotlinx.serialization.Serializable

@Serializable
sealed class ServerMessage {
    /*
    @Serializable
    data class SyncConfig(
        val endpoint: EndpointConfig?,   // null = no endpoint assigned
    ) : ServerMessage()

     */

    @Serializable
    class Probe : ServerMessage()

    @Serializable
    class Ping : ServerMessage()
}