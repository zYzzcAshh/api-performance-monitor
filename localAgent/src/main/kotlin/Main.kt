package org.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    //val intervalSeconds: Long by option("--interval", help = "Interval seconds").long().prompt("Interval seconds")

    override fun run() = runBlocking {
        echo("Registering agent $name")
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

class Root : NoOpCliktCommand(name = "app")

fun main() {
    println("CLI started. Type a command (register, login, agent-register, agent-create-endpoint, info, exit):")
    println("Usage: register --username <u> --password <p>")

    val authStore = AuthStore()
    val agentController = AgentController()

    val root = Root().subcommands(Register(), Login(authStore), Info(authStore), AgentRegister(authStore), AgentCreateEndpoint(authStore, agentController))

    while (!agentController.isMonitoring()) {
        print("> ")
        val line = readlnOrNull()?.trim() ?: break
        if (line == "exit" || line == "quit") {
            println("Bye!")
            break
        }
        if (line.isBlank()) continue

        try {
            root.parse(line.split("\\s+".toRegex()))
        } catch (e: Exception) {
            println("Command not found!")
        }
    }
    println("Starting monitoring...")
    val agentSocketClient = AgentSocketClient(authStore, agentController)
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + job)

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down agent...")
        job.cancel()
        agentSocketClient.close()
    })

    scope.launch { agentSocketClient.run() }

    runBlocking { job.join() } // keep main() alive until cancelled
}