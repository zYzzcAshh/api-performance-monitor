package pt.isel.api_pm.exceptions

import pt.isel.api_pm.worker.MonitoringWorker

class InvalidIntervalException(
    interval: Long
) : RuntimeException("Invalid interval: $interval. Must be >= ${MonitoringWorker.MINIMUM_INTERVAL_MILLIS} seconds")