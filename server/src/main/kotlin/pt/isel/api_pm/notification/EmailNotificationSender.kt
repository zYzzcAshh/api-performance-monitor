package pt.isel.api_pm.notification

import pt.isel.api_pm.utils.SmtpEmailSender

class EmailNotificationSender(
    private val smtpEmailSender: SmtpEmailSender,
    private val to: String,
    private val subject: String,
) : NotificationSender {

    override suspend fun send(
        endpointName: String
    ) {

        smtpEmailSender.send(
            to,
            subject,
            NotificationMessageBuilder.build(
                endpointName
            )
        )
    }
}