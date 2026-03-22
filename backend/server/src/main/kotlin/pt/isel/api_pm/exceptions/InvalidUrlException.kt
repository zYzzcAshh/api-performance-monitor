package pt.isel.api_pm.exceptions

class InvalidUrlException(
    url: String
) : RuntimeException("Invalid URL: $url")