package cu.suitetecsa.sdk.nauta.util

import java.text.SimpleDateFormat
import java.util.*

private const val KILOBYTE = 1024
private const val MEGABYTE = KILOBYTE * KILOBYTE
private const val GIGABYTE = MEGABYTE * KILOBYTE
private const val SECONDS_PER_MINUTE = 60

/**
 * Converts a string representation of a size to bytes.
 *
 * @return The size in bytes.
 * @throws IllegalArgumentException if the size unit is not valid.
 */
fun String.toBytes(): Double {
    val sizeUnit = this.split(" ").last()
    val sizeValue = this.replace(" $sizeUnit", "").replace(" ", "")
    return convertToBytes(sizeValue, sizeUnit.uppercase(Locale.getDefault()))
}

/**
 * Converts a string representation of a date to a [SimpleDateFormat] object.
 *
 * @return The parsed [SimpleDateFormat] object.
 */
fun String.toDate(): Date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { isLenient = false }.parse(this)

fun String.toDateTime(): Date =
    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(this)

fun String.toPriceFloat(): Float = this
    .replace("$", "")
    .replace(",", ".")
    .replace(" CUP", "")
    .replace(" ", "")
    .toFloatOrNull() ?: throw IllegalArgumentException("El formato de la cadena no es correcto")

fun String.toSeconds() = this.split(":").fold(0L) { acc, s -> acc * SECONDS_PER_MINUTE + s.toLong() }

private fun convertToBytes(sizeValue: String, sizeUnit: String): Double {
    return sizeValue.replace(",", ".").toDouble() * when (sizeUnit) {
        "KB" -> KILOBYTE.toDouble()
        "MB" -> MEGABYTE.toDouble()
        "GB" -> GIGABYTE.toDouble()
        else -> throw IllegalArgumentException("La unidad de tamaño no es válida")
    }.toLong()
}
