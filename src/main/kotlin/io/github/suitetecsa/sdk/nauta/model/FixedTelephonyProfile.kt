package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FixedTelephonyProfile(
    val id: String,
    @Json(name = "Listas") val lists: FixedTelephonyLists?,
    @Json(name = "Número de teléfono") val phoneNumber: String,
)
