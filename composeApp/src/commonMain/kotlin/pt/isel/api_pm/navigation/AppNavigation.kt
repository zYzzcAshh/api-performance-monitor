package pt.isel.api_pm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.screen.EndpointsScreen
import pt.isel.api_pm.screen.LoginScreen
import pt.isel.api_pm.screen.RegisterScreen
import pt.isel.api_pm.viewmodel.AuthViewModel
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun AppNavigation(
    api: ApiClient
) {

    val authViewModel = remember {
        AuthViewModel(api)
    }

    val navController = rememberNavController()

    val authState by authViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {

            LoginScreen(
                viewModel = authViewModel,

                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },

                onLoginSuccess = {
                    navController.navigate(Screen.Endpoints.route) {

                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Register.route) {

            RegisterScreen(
                viewModel = authViewModel,

                onNavigateToLogin = {
                    navController.popBackStack()
                },

                onRegisterSuccess = {
                    navController.navigate(Screen.Endpoints.route) {

                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Endpoints.route) {

            authState.token?.let { token ->

                val endpointsViewModel = remember(token) {
                    EndpointsViewModel(api, token)
                }

                EndpointsScreen(
                    viewModel = endpointsViewModel,

                    onLogout = {

                        authViewModel.logout()

                        navController.navigate(Screen.Login.route) {

                            popUpTo(Screen.Endpoints.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}