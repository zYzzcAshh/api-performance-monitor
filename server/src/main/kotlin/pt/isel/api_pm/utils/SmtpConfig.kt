package pt.isel.api_pm.utils

object SmtpConfig {

    val USER =
        System.getenv("SMTP_USER")
            ?: error("SMTP_USER environment variable is missing")

    val PASSWORD =
        System.getenv("SMTP_PASSWORD")
            ?: error("SMTP_PASSWORD environment variable is missing")
}