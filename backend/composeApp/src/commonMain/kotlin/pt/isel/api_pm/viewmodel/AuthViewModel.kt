package pt.isel.api_pm.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.api_pm.api.ApiClient

data class AuthState(
    val token: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class AuthViewModel(
    private val api: ApiClient
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(username: String, password: String) {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true, message = null)

            val result = api.login(username, password)

            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    token = result.getOrNull(),
                    isLoading = false,
                    message = "Login successful"
                )
            } else {
                _state.value.copy(
                    isLoading = false,
                    message = "Error: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun register(username: String, password: String) {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true, message = null)

            val result = api.register(username, password)

            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    isLoading = false,
                    message = "Registered successfully"
                )
            } else {
                _state.value.copy(
                    isLoading = false,
                    message = "Error: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }
}