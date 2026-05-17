package org.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.api.ApiClient

private val apiClient = ApiClient()

class Register(val authStore: AuthStore) : CliktCommand() {
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
        echo("User token being stored right now is $token")
    }
}

class Root : NoOpCliktCommand(name = "app")

fun main() {
    println("CLI started. Type a command (register, login, info, exit):")
    println("Usage: register --username <u> --password <p>")

    val authStore = AuthStore()

    val root = Root().subcommands(Register(authStore), Login(authStore), Info(authStore))

    while (true) {
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
            println("Unexpected error: ${e.message}")
        }
    }
}