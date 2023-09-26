package cu.suitetecsa.sdk.nauta.util

import java.text.NumberFormat
import java.util.*

fun Float.toPriceString(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)
