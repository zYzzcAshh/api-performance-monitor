package org.api

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.api.commands.AgentCreateEndpoint
import org.api.commands.AgentRegister
import org.api.commands.Clear
import org.api.commands.Help
import org.api.commands.Info
import org.api.commands.Login
import org.api.commands.Logout
import org.api.commands.Register

class Root : NoOpCliktCommand(name = "app")

fun main() {
    println("CLI started. Type a command (help, register, login, logout, agent-register, agent-create-endpoint, info, exit):")
    println("Usage: register --username <u> --password <p>")

    val propertiesFileStore = PropertiesFileStore()
    val authStore = AuthStore(propertiesFileStore)
    val agentController = AgentController(propertiesFileStore)

    val root = Root().subcommands(
        Help(),
        Clear(authStore, agentController),
        Register(),
        Login(authStore),
        Logout(authStore, agentController),
        Info(authStore),
        AgentRegister(authStore),
        AgentCreateEndpoint(authStore, agentController)
    )

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
        } catch (_: Exception) {
            println("Error: Invalid command or arguments. Type 'help' for a list of commands.")
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

    runBlocking { job.join() }
}