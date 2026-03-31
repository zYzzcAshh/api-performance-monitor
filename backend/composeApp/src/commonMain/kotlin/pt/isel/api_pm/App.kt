package pt.isel.api_pm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import io.ktor.serialization.kotlinx.json.json
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.RegisterRequest

private val httpClient = HttpClient{
    install(ContentNegotiation) {
        json()
    }
}

const val BASE_URL = "http://localhost:8080/api"

suspend fun registerUser(username: String, password: String): Result<String> =
    runCatching {
        val response = httpClient.post("$BASE_URL/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username, password))
        }
        response.body()
    }

suspend fun loginUser(username: String, password: String): Result<String> =
    runCatching {
        val response = httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }
        response.body()
    }

private enum class Screen { LOGIN, REGISTER }

@Composable
@Preview
fun App() {
    MaterialTheme {
       var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

        when (currentScreen) {
            Screen.LOGIN -> LoginScreen(onNavigateToRegister = { currentScreen = Screen.REGISTER })
            Screen.REGISTER -> RegisterScreen(onNavigateToLogin = { currentScreen = Screen.LOGIN })
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
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
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
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    AuthScreen(
        title = "Login",
        buttonLabel = "Login",
        switchLabel = "Don't have an account? Register",
        onSubmit = { u, p -> loginUser(u, p) },
        onSwitch = onNavigateToRegister,
    )
}

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    AuthScreen(
        title = "Register",
        buttonLabel = "Register",
        switchLabel = "Already have an account? Login",
        onSubmit = { u, p -> registerUser(u, p) },
        onSwitch = onNavigateToLogin,
    )
}