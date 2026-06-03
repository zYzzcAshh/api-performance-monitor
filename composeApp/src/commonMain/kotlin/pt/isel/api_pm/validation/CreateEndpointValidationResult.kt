package pt.isel.api_pm.validation

data class CreateEndpointValidationResult(
    val valid: Boolean,
    val error: String? = null
)