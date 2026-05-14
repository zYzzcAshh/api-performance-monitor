package org.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int

class Register : CliktCommand() {
    val username: String by option("-u", "--username").prompt("Username")
    val password: String by option("-p", "--password").prompt("Password", hideInput = true)

    override fun run() {
        echo("Registering user '$username' password '$password'...")
    }
}

class Login : CliktCommand() {
    val username: String by option("-u", "--username").prompt("Username")
    val password: String by option("-p", "--password").prompt("Password", hideInput = true)

    override fun run() {
        echo("Logging in as '$username' password '$password'...")
    }
}

class Root : NoOpCliktCommand(name = "app")

fun main() {
    println("CLI started. Type a command (register, login, exit):")
    println("Usage: register --username <u> --password <p>")

    val root = Root().subcommands(Register(), Login())

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