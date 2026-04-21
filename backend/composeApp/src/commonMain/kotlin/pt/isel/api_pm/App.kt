package pt.isel.api_pm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import pt.isel.api_pm.api.ApiClient

private val httpClient = HttpClient{
    install(ContentNegotiation) {
        json()
    }
}

const val BASE_URL = "http://localhost:8080/api"

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Endpoints : Screen("endpoints")
}

@Composable
@Preview
fun App() {

    val api = remember { ApiClient(httpClient) }
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

@Composable
private fun AuthScreen(
    title: String,
    buttonLabel: String,
    switchLabel: String,
    onSubmit: suspend (String, String) -> Result<String>,
    onSwitch: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun submit() {
        if (!isLoading && username.isNotBlank() && password.isNotBlank()) {
            scope.launch {
                isLoading = true
                statusMessage = ""
                val result = onSubmit(username, password)
                statusMessage = result.fold(
                    onSuccess = { it },
                    onFailure = { "Error: ${it.message}" },
                )
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeContentPadding()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    submit()
                }
            ),
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                submit()
            },
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            else Text(buttonLabel)
        }

        if (statusMessage.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = statusMessage,
                color = if (statusMessage.startsWith("Error"))
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onSwitch) { Text(switchLabel) }
    }
}

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

@Composable
fun EndpointsScreen(
    api: ApiClient,
    token: String
) {
    var endpoints by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val result = api.getEndpoints(token)
            if (result.isSuccess) {
                endpoints = result.getOrNull()
            } else {
                error = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> CircularProgressIndicator()

            error != null -> Text("Error: $error")

            endpoints != null -> {
                Text("Endpoints:")
                Spacer(Modifier.height(8.dp))
                Text(endpoints!!)
            }
        }
    }
}