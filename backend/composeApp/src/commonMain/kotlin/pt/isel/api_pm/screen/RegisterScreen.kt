package pt.isel.api_pm.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    AuthScreen(
        title = "Register",
        buttonLabel = "Register",
        switchLabel = "Already have an account? Login",
        isLoading = state.isLoading,
        message = state.message,
        onSubmit = { u, p ->
            viewModel.register(u, p)
        },
        onSwitch = onNavigateToLogin
    )
}