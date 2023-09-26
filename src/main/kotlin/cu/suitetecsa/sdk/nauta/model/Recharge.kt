package cu.suitetecsa.sdk.nauta.model

import java.util.Date

data class Recharge(
    val date: Date,
    val import: Float,
    val channel: String,
    val type: String
)
