package pt.isel.api_pm.exceptions

class InvalidIntervalException(
    interval: Long
) : RuntimeException("Invalid interval: $interval. Must be >= 50 seconds")