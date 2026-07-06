package org.api.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.coroutines.runBlocking
import org.api.AgentController
import org.api.AgentMonitoredEndpoint
import org.api.AuthStore
import pt.isel.api_pm.alert.AggregationType
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.alert.ComparisonOperator
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.domain.endpoint.DurationSeconds
import pt.isel.api_pm.domain.endpoint.toHttpMethod
import pt.isel.api_pm.notification.NotificationConfig

private val apiClient = ApiClient()

class AgentCreateEndpoint(val authStore: AuthStore, val agentController: AgentController) : CliktCommand(name = "agent-create-endpoint") {
    val name: String by option("--name", help = "Name of the endpoint to create").prompt("Endpoint name")
    val url: String by option("--url", help = "Url of the endpoint").prompt("URL")
    val method: String by option("--method", help = "Method of the endpoint").prompt("Method")
    val intervalSeconds: Long by option("--interval", help = "Interval seconds").long().prompt("Interval seconds")

    val notificationType: String by option(
        "--notification",
        help = "Notification channel: none, log, discord, slack, email"
    ).prompt("Notification type (none/log/discord/slack/email)", default = "none")

    val webhookUrl: String? by option("--webhook-url", help = "Webhook URL (discord/slack)")
    val email: String? by option("--email", help = "Email address (email notification)")
    val subject: String? by option("--subject", help = "Email subject (email notification)")

    val alertType: String by option(
        "--alert-type",
        help = "Alert rule type: status-code, latency, down-time"
    ).prompt("Alert type (status-code/latency/down-time)", default = "none")

    val alertOperator: String? by option(
        "--alert-operator",
        help = "Comparison operator: gt, gte, lt, lte, eq"
    )
    val alertValue: String? by option("--alert-value", help = "Threshold value for the alert rule")
    val alertDuration: Long by option("--alert-duration", help = "Duration in seconds")
        .long()
        .default(60)
    val aggregation: String? by option(
        "--aggregation",
        help = "Aggregation: any, average, count"
    )
    val aggregationCount: Int? by option("--aggregation-count", help = "Occurrences count").int()

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

        val notification = buildNotification() ?: return@runBlocking

        val alertRule = if (notification == NotificationConfig.None) {
            null
        } else {
            buildAlertRule()
        }

        val response = apiClient.createAgentEndpoint(
            authStore.getAgentToken()!!,
            name,
            method.toHttpMethod(),
            intervalSeconds,
            notification,
            alertRule
        )

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

    private fun buildNotification(): NotificationConfig? {
        return when (notificationType.lowercase()) {
            "none" -> NotificationConfig.None
            "log" -> NotificationConfig.Log
            "discord" -> {
                val webhook = webhookUrl
                if (webhook.isNullOrBlank()) {
                    echo("Error: --webhook-url is required for discord notifications")
                    return null
                }
                NotificationConfig.DiscordWebhook(webhook)
            }
            "slack" -> {
                val webhook = webhookUrl
                if (webhook.isNullOrBlank()) {
                    echo("Error: --webhook-url is required for slack notifications")
                    return null
                }
                NotificationConfig.SlackWebhook(webhook)
            }
            "email" -> {
                val addr = email
                val subj = subject
                if (addr.isNullOrBlank() || subj.isNullOrBlank()) {
                    echo("Error: --email and --subject are required for email notifications")
                    return null
                }
                NotificationConfig.Email(addr, subj)
            }
            else -> {
                echo("Error: unknown notification type `$notificationType`")
                null
            }
        }
    }

    private fun buildAlertRule(): AlertRule? {
        val operator = when (alertOperator?.lowercase()) {
            "gt" -> ComparisonOperator.GT
            "gte" -> ComparisonOperator.GTE
            "lt" -> ComparisonOperator.LT
            "lte" -> ComparisonOperator.LTE
            "eq" -> ComparisonOperator.EQ
            null -> ComparisonOperator.GT
            else -> {
                echo("Error: unknown alert operator `$alertOperator`")
                return null
            }
        }

        val aggregationType = when (aggregation?.lowercase()) {
            "any" -> AggregationType.ALL
            "average" -> AggregationType.AVG
            "count", null -> AggregationType.COUNT(aggregationCount ?: 1)
            else -> {
                echo("Error: unknown aggregation `$aggregation`")
                return null
            }
        }

        return when (alertType.lowercase()) {
            "status-code" -> AlertRule.StatusCodeRule(
                operator = operator,
                value = alertValue?.toIntOrNull() ?: 500,
                durationSeconds = DurationSeconds(alertDuration),
                aggregation = aggregationType
            )
            "latency" -> AlertRule.LatencyRule(
                operator = operator,
                value = alertValue?.toLongOrNull() ?: 1000,
                durationSeconds = DurationSeconds(alertDuration),
                aggregation = aggregationType
            )
            "down-time" -> AlertRule.DownTimeRule(
                durationSeconds = DurationSeconds(alertDuration)
            )
            "none" -> null
            else -> {
                echo("Error: unknown alert type `$alertType`")
                null
            }
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

class Logout(val authStore: AuthStore, val agentController: AgentController) : CliktCommand() {
    override fun run() {
        authStore.clear()
        agentController.clear()
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

class Clear(val authStore: AuthStore, val agentController: AgentController) : CliktCommand() {
    override fun run() {
        echo("Clearing data...")
        authStore.clear()
        agentController.clear()
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