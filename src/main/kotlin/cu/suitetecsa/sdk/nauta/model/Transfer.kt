package cu.suitetecsa.sdk.nauta.model

import java.util.Date

data class Transfer(
    val date: Date,
    val import: Float,
    val destinyAccount: String
)
