package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the profile information of a mobile user, including account status, balances, services, and other details.
 *
 * @property id The unique identifier for the mobile profile.
 * @property lte The status or availability of 4G (LTE) for the mobile network.
 * @property advanceBalance The amount available for advance balance service.
 * @property status The account status of the mobile subscriber.
 * @property lockDate The date when the account was locked, if applicable.
 * @property deletionDate The scheduled date for account deletion, if applicable.
 * @property saleDate The date when the mobile service was purchased.
 * @property internet The Internet usage status or package information.
 * @property lists A collection containing mobile plans and bonuses associated with the account.
 * @property currency The currency used for the mobile account balances and services.
 * @property phoneNumber The associated mobile phone number.
 * @property mainBalance The primary account balance available for usage.
 * @property consumptionRate The charging rate per unit of consumption.
 */
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
