package pt.isel.api_pm.app.module

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.worker.MonitoringWorker

// For now just 60 seconds
val INTERVAL_SECONDS_LIST =
    listOf(60L, 120L, 180L, 300L, 600L, 900L, 1200L, 1800L)
        .subList(0, 1)

fun Application.monitoringModule(
    monitoringService: MonitoringService,
    metricsService: MetricsService,
    endpointService: EndpointService,
) {
    val monitoringScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    INTERVAL_SECONDS_LIST.forEach { intervalSeconds ->
        val worker = MonitoringWorker(monitoringService, metricsService, endpointService, intervalSeconds)
        worker.start(monitoringScope)
    }

    monitor.subscribe(ApplicationStopped) {
        monitoringScope.cancel()
    }
}
