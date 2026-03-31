package pt.isel.api_pm.app.module

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import pt.isel.api_pm.domain.endpoint.INTERVAL_SECONDS_LIST
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.worker.MonitoringWorker

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
