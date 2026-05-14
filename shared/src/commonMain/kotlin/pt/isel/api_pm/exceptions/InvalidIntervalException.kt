package pt.isel.api_pm.exceptions

class InvalidIntervalException(
    interval: Long,
) : Exception("Invalid interval: $interval. Must be (60, 120, 180, 300, 600, 900, 1200, 1800) seconds")
