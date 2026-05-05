package pt.isel.api_pm.exceptions

class DurationValueException(
    value: Long
) : Exception("Duration value must be between 0 and 604800 seconds (7 days), but was $value")