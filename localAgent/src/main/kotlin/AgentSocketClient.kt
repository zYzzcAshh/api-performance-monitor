package org.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.json.Json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import pt.isel.api_pm.dto.AgentMessage
import pt.isel.api_pm.dto.ServerMessage

class AgentSocketClient(
    private val authStore: AuthStore,
    private val agentController: AgentController,
) {
    private val client = HttpClient { install(WebSockets) }

    suspend fun run() {
        var backoffMs = 1000L
        val maxBackoffMs = 30000L

        while (currentCoroutineContext().isActive) {
            try {
                connectAndListen()
                backoffMs = 1000L
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                println("Agent connection lost: ${e.message}. Retrying in ${backoffMs}ms...")
            }
            delay(backoffMs)
            backoffMs = (backoffMs * 2).coerceAtMost(maxBackoffMs)
        }
    }

    private suspend fun connectAndListen() {
        val token = authStore.getAgentToken() ?: return
        client.webSocket(
            request = {
                url("ws://localhost:8080/ws/agent")
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        ) {
            for (frame in incoming) {
                if (frame !is Frame.Text) continue
                when (Json.decodeFromString<ServerMessage>(frame.readText())) {
                    is ServerMessage.DoRequest -> {
                        val endpoint = agentController.getMonitoredEndpoint() ?: continue
                        val result = performRequest(endpoint)
                        send(Frame.Text(Json.encodeToString<AgentMessage>(result)))
                    }
                }
            }
        }
    }

    private suspend fun performRequest(endpoint: AgentMonitoredEndpoint): AgentMessage.Metrics {
        val start = System.currentTimeMillis()
        val response = client.get(endpoint.url)
        val latency = System.currentTimeMillis() - start
        println("Local agent performed a request for ${endpoint.url} and got: ${response.status.value} status code; ${latency}ms latency")
        return AgentMessage.Metrics(endpoint.name, response.status.value, latency, System.currentTimeMillis())
    }

    fun close() = client.close()
}