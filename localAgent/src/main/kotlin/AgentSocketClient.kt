package org.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.json.Json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.message.ServerMessage
import kotlin.time.Clock.System.now

class AgentSocketClient(
    private val authStore: AuthStore,
    private val agentController: AgentController,
) {
    private val client = HttpClient { install(WebSockets) }

    suspend fun run() {
        var backoffMs = 1000L
        var retries = 0
        val maxBackoffMs = 30000L
        val maxRetries = 10

        while (currentCoroutineContext().isActive) {
            try {
                connectAndListen()
                backoffMs = 1000L
            } catch (e: CancellationException) {
                close()
                throw e
            } catch (e: Exception) {
                if (retries++ >= maxRetries) {
                    println("Max retries reached. Exiting...")
                    close()
                    break
                }
                println("Agent connection lost: ${e.message}. ($retries/$maxRetries retries) Retrying in ${backoffMs}ms...")
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
        val response = when (endpoint.method) {
            HttpMethod.GET -> client.get(endpoint.url)
            HttpMethod.POST -> client.post(endpoint.url)
            HttpMethod.PUT -> client.put(endpoint.url)
            HttpMethod.DELETE -> client.delete(endpoint.url)
        }
        val latency = System.currentTimeMillis() - start
        println("Local agent performed a request for ${endpoint.url} and got: ${response.status.value} status code; ${latency}ms latency")
        return AgentMessage.Metrics(endpoint.name, response.status.value, latency, now())
    }

    fun close() {
        client.close()
    }
}