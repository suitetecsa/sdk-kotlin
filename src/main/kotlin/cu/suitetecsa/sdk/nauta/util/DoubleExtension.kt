package cu.suitetecsa.sdk.nauta.util

private const val MAX_UNIT_VALUE = 1024

fun Double.toSizeString(): String {
    val sizeUnits = arrayOf("bytes", "KB", "MB", "GB", "TB")
    var sizeValue = this
    var sizeUnitIndex = 0
    while (sizeValue >= MAX_UNIT_VALUE && sizeUnitIndex < sizeUnits.lastIndex) {
        sizeValue /= MAX_UNIT_VALUE
        sizeUnitIndex++
    }
    return "%.2f %s".format(sizeValue).replace(".", ",") + " " + sizeUnits[sizeUnitIndex]
}
