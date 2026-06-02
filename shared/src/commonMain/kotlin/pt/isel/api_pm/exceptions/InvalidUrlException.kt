package pt.isel.api_pm.exceptions

class InvalidUrlException(
    url: String,
) : Exception(
    "Invalid URL '$url'. URL must start with http:// or https://"
)
