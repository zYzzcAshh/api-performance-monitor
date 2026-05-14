package pt.isel.api_pm.exceptions

class DuplicateEndpointException(
    url: String,
) : Exception("Endpoint with URL '$url' already exists")
