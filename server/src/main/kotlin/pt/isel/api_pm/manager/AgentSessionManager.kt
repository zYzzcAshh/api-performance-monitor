package pt.isel.api_pm.manager

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import pt.isel.api_pm.dto.message.ServerMessage
import pt.isel.api_pm.repo.AgentRepository

class AgentSessionManager(
    private val repo: AgentRepository,
) {
    private suspend fun send(session: DefaultWebSocketServerSession, msg: ServerMessage) {
        try {
            session.send(Frame.Text(msg.toString()))
        } catch (_: Exception) { /* session gone */ }
    }
}