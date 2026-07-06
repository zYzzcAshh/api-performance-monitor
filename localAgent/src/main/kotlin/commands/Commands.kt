package org.api.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.coroutines.runBlocking
import org.api.AgentController
import org.api.AgentMonitoredEndpoint
import org.api.AuthStore
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.domain.endpoint.toHttpMethod

private val apiClient = ApiClient()

class AgentCreateEndpoint(val authStore: AuthStore, val agentController: AgentController) : CliktCommand(name = "agent-create-endpoint") {
    val name: String by option("--name", help = "Name of the endpoint to create").prompt("Endpoint name")
    val url: String by option("--url", help = "Url of the endpoint").prompt("URL")
    val method: String by option("--method", help = "Method of the endpoint").prompt("Method")
    val intervalSeconds: Long by option("--interval", help = "Interval seconds").long().prompt("Interval seconds")

    override fun run() = runBlocking {
        // Just temporary
        if (method != "GET" && method != "PUT" && method != "POST" && method != "DELETE") {
            echo("Method `$method` is not allowed")
            return@runBlocking
        }
        echo("Creating agent endpoint")
        if (authStore.getAgentToken() == null) {
            echo("You need to register an agent first!")
            return@runBlocking
        }
        val response = apiClient.createAgentEndpoint(authStore.getAgentToken()!!, name, method.toHttpMethod(), intervalSeconds)

        if (response.isSuccess) {
            echo("Successfully created agent endpoint")
            val agentMonitoredEndpoint = AgentMonitoredEndpoint(
                name,
                method.toHttpMethod(),
                url,
                intervalSeconds,
            )
            agentController.setMonitoredEndpoint(agentMonitoredEndpoint)
            agentController.setMonitoring(true)
        } else {
            echo("Failed to create agent endpoint")
        }
    }
}

class AgentRegister(val authStore: AuthStore) : CliktCommand(name = "agent-register") {
    val name: String by option("--name", help = "Name of the agent").prompt("Agent name")

    override fun run() = runBlocking {
        echo("Registering agent $name")
        if (authStore.getAgentToken() != null) {
            echo("There is already an agent registered")
            return@runBlocking
        }
        if (authStore.getToken() == null) {
            echo("You need to login first!")
            return@runBlocking
        }
        val response = apiClient.agentRegister(authStore.getToken()!!, name)

        if (response.isSuccess) {
            echo("Successfully registered agent $name")
            authStore.setAgentToken(response.getOrThrow())
        } else {
            echo("Failed to registered agent $name")
        }
    }
}

class Register() : CliktCommand() {
    val username: String by option("-u", "--username").prompt("Username")
    val password: String by option("-p", "--password").prompt("Password", hideInput = true)

    override fun run() = runBlocking {
        echo("Registering username '$username'...")

        val response = apiClient.register(username, password)

        if (response.isSuccess) {
            echo("Successfully registered!")
        } else {
            echo("Failed to register!")
        }
    }
}

class Logout(val authStore: AuthStore) : CliktCommand() {
    override fun run() {
        authStore.clearToken()
        authStore.clearAgentToken()
        echo("Successfully logged out!")
    }
}

class Login(val authStore: AuthStore) : CliktCommand() {
    val username: String by option("-u", "--username").prompt("Username")
    val password: String by option("-p", "--password").prompt("Password", hideInput = true)

    override fun run() = runBlocking {
        echo("Logging in username '$username'...")

        val response = apiClient.login(username, password)

        if (response.isSuccess) {
            echo("Successfully logged in!")
            authStore.setToken(response.getOrThrow())
        } else {
            echo("Failed to log in!")
        }
    }
}

class Info(val authStore: AuthStore) : CliktCommand() {
    override fun run() {
        val token = authStore.getToken()
        echo("User token being stored right now is: $token")
        val agentToken = authStore.getAgentToken()
        echo("Agent token being stored right now is: $agentToken")
    }
}

class Help() : CliktCommand() {
    override fun run() {
        echo(
            """
            Authentication
              register
                  Register a new user.

              login
                  Login to your account.

              logout
                  Logout and clear stored credentials.

            Agent
              agent-register
                  Register a monitoring agent.

              agent-create-endpoint
                  Create an endpoint and start monitoring it.

            Utility
              info
                  Display stored user and agent tokens.

              help
                  Show this help message.

              exit
                  Exit the application.

            ------------------------------------------
            Examples

              register --username alice --password secret

              login --username alice --password secret

              agent-register --name "Home PC"

              agent-create-endpoint \
                  --name "Google" \
                  --url "https://google.com" \
                  --method GET \
                  --interval 60
            ------------------------------------------
            """.trimIndent()
        )
    }
}