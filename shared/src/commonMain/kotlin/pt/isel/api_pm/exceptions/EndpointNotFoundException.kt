package pt.isel.api_pm.exceptions

class EndpointNotFoundException(
    override val message: String = "Endpoint not found"
) : Exception(message)