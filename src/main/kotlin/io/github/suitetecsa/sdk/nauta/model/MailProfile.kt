package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MailProfile(
    val id: String,
    @Json(name = "Cuenta de correo") val emailAccount: String,
    @Json(name = "Fecha de venta") val saleDate: String,
    @Json(name = "Moneda") val currency: String
)
