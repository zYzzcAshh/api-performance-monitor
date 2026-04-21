package pt.isel.api_pm.screen

import androidx.compose.runtime.Composable
import pt.isel.api_pm.api.ApiClient

@Composable
fun LoginScreen(
    api: ApiClient,
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    AuthScreen(
        title = "Login",
        buttonLabel = "Login",
        switchLabel = "Don't have an account? Register",
        onSubmit = { u, p ->
            val result = api.login(u, p)
            if (result.isSuccess) {
                result.getOrNull()?.let { onLoginSuccess(it) }
            }
            result
        },
        onSwitch = onNavigateToRegister,
    )
}