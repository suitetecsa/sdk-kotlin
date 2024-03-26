package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupplementaryService(
    @Json(name = "Servicio") val service: String,
    @Json(name = "Valor") val value: String,
)
