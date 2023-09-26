package cu.suitetecsa.sdk.nauta.util

// Define constants for the magic numbers
private const val SECONDS_IN_HOUR = 3600
private const val SECONDS_IN_MINUTE = 60

fun Long.toTimeString(): String {
    val hours = this / SECONDS_IN_HOUR
    val minutes = (this % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
    val seconds = this % SECONDS_IN_MINUTE
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}
