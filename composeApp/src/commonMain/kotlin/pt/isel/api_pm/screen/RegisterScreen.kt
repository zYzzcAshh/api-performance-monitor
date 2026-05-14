package pt.isel.api_pm.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.isel.api_pm.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    AuthScreen(
        title = "Register",
        buttonLabel = "Register",
        switchLabel = "Back to login",
        isLoading = state.isLoading,
        message = state.message,
        onSubmit = { u, p ->
            viewModel.register(u, p)
        },
        onSwitch = onNavigateToLogin
    )

    LaunchedEffect(state.token) {

        if (state.token != null) {
            onRegisterSuccess()
        }
    }
}