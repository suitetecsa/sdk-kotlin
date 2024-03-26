package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NavProfile(
    val id: String,
    @Json(name = "Bonificación por disfrutar") val bonusToEnjoy: String,
    @Json(name = "Cuenta de acceso") val accessAccount: String,
    @Json(name = "Estado") val status: String,
    @Json(name = "Fecha de bloqueo") val lockDate: String,
    @Json(name = "Fecha de eliminación") val deletionDate: String,
    @Json(name = "Fecha de venta") val saleDate: String,
    @Json(name = "Horas de bonificación") val bonusHours: String,
    @Json(name = "Moneda") val currency: String,
    @Json(name = "saldo") val balance: String,
    @Json(name = "Tipo de acceso") val accessType: String,
)
