package pt.isel.api_pm.validation

import pt.isel.api_pm.notification.NotificationConfig

object CreateEndpointValidator {

    fun validate(
        name: String,
        url: String,
        interval: String,
        notification: NotificationConfig
    ): CreateEndpointValidationResult {

        if (name.isBlank()) {
            return CreateEndpointValidationResult(
                valid = false,
                error = "Endpoint name is required"
            )
        }

        if (url.isBlank()) {
            return CreateEndpointValidationResult(
                valid = false,
                error = "URL is required"
            )
        }

        if (
            !url.startsWith("https://") &&
            !url.startsWith("http://")
        ) {
            return CreateEndpointValidationResult(
                valid = false,
                error = "URL must start with http:// or https://"
            )
        }

        val intervalInt =
            interval.toIntOrNull()
                ?: return CreateEndpointValidationResult(
                    valid = false,
                    error = "Interval must be a number"
                )

        if (intervalInt < 60) {
            return CreateEndpointValidationResult(
                valid = false,
                error = "Interval must be at least 60 seconds"
            )
        }

        when (notification) {

            is NotificationConfig.DiscordWebhook -> {

                if (notification.webhookUrl.isBlank()) {
                    return CreateEndpointValidationResult(
                        valid = false,
                        error = "Discord webhook is required"
                    )
                }
            }

            is NotificationConfig.SlackWebhook -> {

                if (notification.webhookUrl.isBlank()) {
                    return CreateEndpointValidationResult(
                        valid = false,
                        error = "Slack webhook is required"
                    )
                }
            }

            is NotificationConfig.Email -> {

                if (notification.to.isBlank()) {
                    return CreateEndpointValidationResult(
                        valid = false,
                        error = "Email is required"
                    )
                }

                if (
                    !notification.to.contains("@") ||
                    !notification.to.contains(".")
                ) {
                    return CreateEndpointValidationResult(
                        valid = false,
                        error = "Invalid email"
                    )
                }

                if (notification.subject.isBlank()) {
                    return CreateEndpointValidationResult(
                        valid = false,
                        error = "Email subject is required"
                    )
                }
            }

            else -> Unit
        }

        return CreateEndpointValidationResult(
            valid = true
        )
    }
}