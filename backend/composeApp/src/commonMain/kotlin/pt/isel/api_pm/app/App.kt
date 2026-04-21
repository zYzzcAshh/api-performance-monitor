package pt.isel.api_pm.app

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.screen.EndpointsScreen
import pt.isel.api_pm.screen.LoginScreen
import pt.isel.api_pm.screen.RegisterScreen
import pt.isel.api_pm.screen.Screen

const val BASE_URL = "http://localhost:8080/api"

@Composable
@Preview
fun App() {
    val api = remember { ApiClient() }
    var token by remember { mutableStateOf<String?>(null) }

    val navController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    api = api,
                    onLoginSuccess = { receivedToken ->
                        token = receivedToken
                        navController.navigate(Screen.Endpoints.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    api = api,
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Endpoints.route) {
                token?.let {
                    EndpointsScreen(api, it)
                }
            }
        }
    }
}