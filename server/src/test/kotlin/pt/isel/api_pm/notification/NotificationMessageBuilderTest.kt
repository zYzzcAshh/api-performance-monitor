package pt.isel.api_pm.notification

import kotlin.test.Test
import kotlin.test.assertTrue

class NotificationMessageBuilderTest {

    @Test
    fun `message contains endpoint name`() {

        val message =
            NotificationMessageBuilder.build(
                "github-api"
            )

        assertTrue(
            message.contains("github-api")
        )
    }

    @Test
    fun `message contains alert text`() {

        val message =
            NotificationMessageBuilder.build(
                "github-api"
            )

        assertTrue(
            message.contains("Alert Triggered")
        )
    }

    @Test
    fun `message contains monitor title`() {

        val message =
            NotificationMessageBuilder.build(
                "github-api"
            )

        assertTrue(
            message.contains(
                "API Performance Monitor"
            )
        )
    }

    @Test
    fun `message is not blank`() {

        val message =
            NotificationMessageBuilder.build(
                "github-api"
            )

        assertTrue(
            message.isNotBlank()
        )
    }
}