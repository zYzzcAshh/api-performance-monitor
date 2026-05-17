package pt.isel.api_pm.utils

import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import java.util.Properties

class SmtpEmailSender(
    private val email: String,
    private val password: String
) {
    private val logger = LoggerFactory.getLogger(SmtpEmailSender::class.java)

    fun send(to: String, subject: String, body: String) {
        val props = Properties().apply{
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(email, password)
        })

        val message = MimeMessage(session).apply {
            setFrom(email)
            setRecipients(MimeMessage.RecipientType.TO, to)
            setSubject(subject)
            setText(body)
        }

        Transport.send(message)

        logger.info("Email sent from $email to $to")
    }
}