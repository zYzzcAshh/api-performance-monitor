package pt.isel.api_pm.validation

import pt.isel.api_pm.notification.NotificationConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateEndpointValidatorTest {

    @Test
    fun blank_name_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "",
                url = "https://google.com",
                interval = "60",
                notification = NotificationConfig.None
            )

        assertFalse(result.valid)

        assertEquals(
            "Endpoint name is required",
            result.error
        )
    }

    @Test
    fun blank_url_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "",
                interval = "60",
                notification = NotificationConfig.None
            )

        assertFalse(result.valid)

        assertEquals(
            "URL is required",
            result.error
        )
    }

    @Test
    fun invalid_url_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "google.com",
                interval = "60",
                notification = NotificationConfig.None
            )

        assertFalse(result.valid)

        assertEquals(
            "URL must start with http:// or https://",
            result.error
        )
    }

    @Test
    fun invalid_interval_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "abc",
                notification = NotificationConfig.None
            )

        assertFalse(result.valid)

        assertEquals(
            "Interval must be a number",
            result.error
        )
    }

    @Test
    fun interval_below_minimum_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "5",
                notification = NotificationConfig.None
            )

        assertFalse(result.valid)

        assertEquals(
            "Interval must be at least 60 seconds",
            result.error
        )
    }

    @Test
    fun discord_without_webhook_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "60",
                notification =
                    NotificationConfig.DiscordWebhook("")
            )

        assertFalse(result.valid)

        assertEquals(
            "Discord webhook is required",
            result.error
        )
    }

    @Test
    fun slack_without_webhook_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "60",
                notification =
                    NotificationConfig.SlackWebhook("")
            )

        assertFalse(result.valid)

        assertEquals(
            "Slack webhook is required",
            result.error
        )
    }

    @Test
    fun invalid_email_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "60",
                notification =
                    NotificationConfig.Email(
                        to = "invalid-email",
                        subject = "Alert"
                    )
            )

        assertFalse(result.valid)

        assertEquals(
            "Invalid email",
            result.error
        )
    }

    @Test
    fun blank_subject_is_invalid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "60",
                notification =
                    NotificationConfig.Email(
                        to = "test@test.com",
                        subject = ""
                    )
            )

        assertFalse(result.valid)

        assertEquals(
            "Email subject is required",
            result.error
        )
    }

    @Test
    fun valid_input_is_valid() {

        val result =
            CreateEndpointValidator.validate(
                name = "Google",
                url = "https://google.com",
                interval = "60",
                notification = NotificationConfig.None
            )

        assertTrue(result.valid)

        assertEquals(
            null,
            result.error
        )
    }
}