package pt.isel.api_pm.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.api.FakeApi

class EndpointsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadMonitored_success_updates_state() = runTest {

        val api = FakeApi()

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadMonitored()

        advanceUntilIdle()

        assertEquals(
            0,
            vm.state.value.monitoredEndpoints.size
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createEndpoint_invalid_input_sets_message() = runTest {

        val api = FakeApi()

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.createEndpoint(
            "",
            "",
            "0"
        )

        advanceUntilIdle()

        assertEquals(
            "Invalid input",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createEndpoint_success_sets_success_message() = runTest {

        val api = FakeApi()

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.createEndpoint(
            "Google",
            "https://google.com",
            "60"
        )

        advanceUntilIdle()

        assertEquals(
            "Created successfully",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteEndpoint_success_sets_message() = runTest {

        val api = FakeApi()

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.deleteEndpoint(1u)

        advanceUntilIdle()

        assertEquals(
            "Endpoint deleted",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadMetrics_success_updates_metrics() = runTest {

        val api = FakeApi()

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadMetrics(1u)

        advanceUntilIdle()

        assertEquals(
            0,
            vm.state.value.metrics.size
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createEndpoint_failure_sets_error_message() = runTest {

        val api = FakeApi().apply {
            createEndpointResult =
                Result.failure(
                    Exception("Invalid URL")
                )
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.createEndpoint(
            "Google",
            "invalid",
            "60"
        )

        advanceUntilIdle()

        assertEquals(
            "Error: Invalid URL",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteEndpoint_failure_sets_error_message() = runTest {

        val api = FakeApi().apply {
            deleteEndpointResult =
                Result.failure(
                    Exception("Delete failed")
                )
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.deleteEndpoint(1u)

        advanceUntilIdle()

        assertEquals(
            "Error deleting endpoint",
            vm.state.value.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadMonitored_failure_sets_error() = runTest {

        val api = FakeApi().apply {
            endpointsResult =
                Result.failure(
                    Exception("Unauthorized")
                )
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadMonitored()

        advanceUntilIdle()

        assertEquals(
            "Unauthorized",
            vm.state.value.error
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadMetrics_failure_sets_error() = runTest {

        val api = FakeApi().apply {
            metricsResult =
                Result.failure(
                    Exception("Metrics error")
                )
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadMetrics(1u)

        advanceUntilIdle()

        assertEquals(
            "Metrics error",
            vm.state.value.error
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadSummary_success_updates_summary() = runTest {

        val summary =
            AggregatedMetric(
                endpoint = EndpointUrl("https://google.com"),
                startTime = Clock.System.now(),
                endTime = Clock.System.now(),
                averageLatency = 100.0,
                totalRequests = 50,
                errorRate = 2.0,
                throughput = 10,
                uptime = 98.0,
                percentile95 = 120,
                percentile99 = 150,
                statusCodeDistribution = mapOf(
                    200 to 48,
                    500 to 2
                )
            )

        val api = FakeApi().apply {
            summaryResult =
                Result.success(summary)
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadSummary(1u)

        advanceUntilIdle()

        assertEquals(
            summary,
            vm.state.value.summary
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadSummary_failure_sets_error() = runTest {

        val api = FakeApi().apply {
            summaryResult =
                Result.failure(
                    Exception("Summary error")
                )
        }

        val vm =
            EndpointsViewModel(
                api = api,
                token = "token",
                scope = this
            )

        vm.loadSummary(1u)

        advanceUntilIdle()

        assertEquals(
            "Summary error",
            vm.state.value.error
        )
    }
}