package pt.isel.api_pm.screen

import androidx.compose.runtime.Composable
import pt.isel.api_pm.api.ApiClient

@Composable
fun RegisterScreen(
    api: ApiClient,
    onNavigateToLogin: () -> Unit
) {
    AuthScreen(
        title = "Register",
        buttonLabel = "Register",
        switchLabel = "Already have an account? Login",
        onSubmit = { u, p -> api.register(u, p) },
        onSwitch = onNavigateToLogin,
    )
}