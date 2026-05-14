package pt.isel.api_pm.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.isel.api_pm.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    AuthScreen(
        title = "Login",
        buttonLabel = "Login",
        switchLabel = "Don't have an account? Register",
        isLoading = state.isLoading,
        message = state.message,
        onSubmit = { u, p ->
            viewModel.login(u, p)
        },
        onSwitch = onNavigateToRegister
    )

    LaunchedEffect(state.token) {
        if (state.token != null) {
            onLoginSuccess()
        }
    }
}