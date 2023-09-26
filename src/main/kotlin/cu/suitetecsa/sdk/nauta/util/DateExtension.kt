package cu.suitetecsa.sdk.nauta.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
