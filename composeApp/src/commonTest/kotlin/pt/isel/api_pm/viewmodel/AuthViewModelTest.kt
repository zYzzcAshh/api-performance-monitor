package pt.isel.api_pm.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pt.isel.api_pm.api.FakeApi

class AuthViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_success_sets_token() = runTest {

        val api = FakeApi()

        val vm =
            AuthViewModel(
                api = api,
                scope = this
            )

        vm.login(
            "user",
            "123456"
        )

        advanceUntilIdle()

        assertEquals(
            "token",
            vm.state.value.token
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_failure_sets_error_message() = runTest {

        val api = FakeApi().apply {
            loginResult =
                Result.failure(
                    Exception("Invalid credentials")
                )
        }

        val vm =
            AuthViewModel(
                api = api,
                scope = this
            )

        vm.login(
            "user",
            "wrong"
        )

        advanceUntilIdle()

        assertEquals(
            "Error: Invalid credentials",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun logout_clears_token() = runTest {

        val api = FakeApi()

        val vm =
            AuthViewModel(
                api = api,
                scope = this
            )

        vm.login(
            "user",
            "123456"
        )

        advanceUntilIdle()

        vm.logout()

        assertEquals(
            null,
            vm.state.value.token
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_sets_token() = runTest {

        val api = FakeApi()

        val vm =
            AuthViewModel(
                api = api,
                scope = this
            )

        vm.register(
            "user",
            "123456"
        )

        advanceUntilIdle()

        assertEquals(
            "token",
            vm.state.value.token
        )
    }
}