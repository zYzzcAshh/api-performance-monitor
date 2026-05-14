package pt.isel.api_pm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.api_pm.components.AppButton
import pt.isel.api_pm.components.AppTextField
import pt.isel.api_pm.components.ScreenContainer
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.theme.Surface
import pt.isel.api_pm.theme.TextSecondary

@Composable
fun AuthScreen(
    title: String,
    buttonLabel: String,
    switchLabel: String,
    isLoading: Boolean,
    message: String?,
    onSubmit: (String, String) -> Unit,
    onSwitch: () -> Unit,
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun submit() {
        if (!isLoading && username.isNotBlank() && password.isNotBlank()) {
            onSubmit(username, password)
        }
    }

    ScreenContainer {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .widthIn(max = 450.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "API Performance Monitor",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Observability platform for monitoring web services.",
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(32.dp))

                    AppTextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        label = "Username",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text("Password")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                submit()
                            }
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(28.dp))

                    AppButton(
                        text = if (isLoading) "Loading..." else buttonLabel,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            submit()
                        }
                    )

                    if (!message.isNullOrEmpty()) {

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = message,
                            color =
                                if (message.startsWith("Error"))
                                    MaterialTheme.colorScheme.error
                                else
                                    Primary
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(
                        onClick = onSwitch
                    ) {
                        Text(
                            switchLabel,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}