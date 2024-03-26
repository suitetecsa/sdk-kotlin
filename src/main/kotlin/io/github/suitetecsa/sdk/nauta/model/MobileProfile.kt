package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MobileProfile(
    val id: String,
    @Json(name = "4G") val lte: String,
    @Json(name = "Adelanta Saldo") val advanceBalance: String,
    @Json(name = "Estado") val status: String,
    @Json(name = "Fecha de Bloqueo") val lockDate: String,
    @Json(name = "Fecha de Eliminación") val deletionDate: String,
    @Json(name = "Fecha de Venta") val saleDate: String,
    @Json(name = "Internet") val internet: String,
    @Json(name = "Listas") val lists: Lists,
    @Json(name = "Moneda") val currency: String,
    @Json(name = "Número de Teléfono") val phoneNumber: String,
    @Json(name = "Saldo Principal") val mainBalance: String,
    @Json(name = "Tarifa por Consumo") val consumptionRate: String,
)
