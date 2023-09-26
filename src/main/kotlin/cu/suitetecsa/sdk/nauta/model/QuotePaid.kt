package cu.suitetecsa.sdk.nauta.model

import java.util.Date

data class QuotePaid(
    val date: Date,
    val import: Float,
    val channel: String,
    val type: String,
    val office: String
)
