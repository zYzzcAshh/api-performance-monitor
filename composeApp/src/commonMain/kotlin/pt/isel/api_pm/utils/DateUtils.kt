package pt.isel.api_pm.utils

fun formatTimestamp(timestamp: String): String {

    val clean =
        timestamp
            .replace("T", " ")
            .substringBefore(".")

    val date = clean.substring(0, 10)
    val time = clean.substring(11)

    val parts = date.split("-")

    return "${parts[2]}/${parts[1]}/${parts[0]} $time"
}