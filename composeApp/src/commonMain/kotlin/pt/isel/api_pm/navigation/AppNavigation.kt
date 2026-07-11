package pt.isel.api_pm.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.domain.endpoint.EndpointUiModel
import pt.isel.api_pm.screen.*
import pt.isel.api_pm.viewmodel.AuthViewModel
import pt.isel.api_pm.viewmodel.EndpointsViewModel

@Composable
fun AppNavigation(
    api: ApiClient
) {
    val authViewModel = remember { AuthViewModel(api) }
    val navController = rememberNavController()
    val authState by authViewModel.state.collectAsState()
    val endpointsViewModel = authState.token?.let { token -> remember(token) { EndpointsViewModel(api, token) } }
    var editingEndpoint by remember { mutableStateOf<EndpointUiModel?>(null) }

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
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Endpoints.route) {
            endpointsViewModel?.let { vm ->
                EndpointsScreen(
                    viewModel = vm,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Endpoints.route) { inclusive = true }
                        }
                    },
                    onCreateMonitoring = {
                        editingEndpoint = null
                        navController.navigate(Screen.CreateMonitoring.route)
                    },
                    onOpenEndpoint = { endpointId ->
                        navController.navigate("${Screen.EndpointDashboard.route}/$endpointId")
                    },
                    onOpenAgents = {
                        navController.navigate(Screen.Agents.route)
                    },
                    onEditEndpoint = { endpoint ->
                        editingEndpoint = endpoint
                        navController.navigate(Screen.CreateMonitoring.route)
                    }
                )
            }
        }

        composable(Screen.CreateMonitoring.route) {
            endpointsViewModel?.let { vm ->
                CreateMonitoringScreen(
                    viewModel = vm,
                    endpoint = editingEndpoint,
                    onBack = {
                        editingEndpoint = null
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = "${Screen.EndpointDashboard.route}/{endpointId}"
        ) { backStackEntry ->
            val endpointId =
                backStackEntry.arguments
                    ?.read {
                        getString("endpointId")
                    }
                    ?.toUInt()
                    ?: return@composable

            endpointsViewModel?.let { vm ->
                EndpointDashboardScreen(
                    endpointId = endpointId,
                    viewModel = vm,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Agents.route) {

            endpointsViewModel?.let { vm ->

                AgentsScreen(
                    viewModel = vm,
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenAgent = { agentId ->
                        navController.navigate("${Screen.AgentDashboard.route}/$agentId")
                    }
                )
            }
        }

        composable(
            route = "${Screen.AgentDashboard.route}/{agentId}"
        ) { backStackEntry ->

            val agentId =
                backStackEntry.arguments
                    ?.read {
                        getString("agentId")
                    }
                    ?.toUInt()
                    ?: return@composable

            endpointsViewModel?.let { vm ->

                AgentDashboardScreen(
                    agentId = agentId,
                    viewModel = vm,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}