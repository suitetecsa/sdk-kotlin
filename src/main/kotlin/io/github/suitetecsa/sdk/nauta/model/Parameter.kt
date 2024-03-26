package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Parameter(
    @Json(name = "nombre") val name: String,
    @Json(name = "orden") val order: String,
    @Json(name = "parametro") val parameter: String,
    @Json(name = "tipo") val type: String,
    @Json(name = "valor") val value: String,
)
