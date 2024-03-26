package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MobilePlan(
    @Json(name = "Datos") val data: String,
    @Json(name = "tipo") val type: String,
    @Json(name = "Vence") val expires: String,
)
