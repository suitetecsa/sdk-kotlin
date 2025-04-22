package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a mobile bonus, typically associated with a user's mobile account.
 *
 * @property data The data allowance provided by the bonus.
 * @property startDate The start date when the bonus becomes active.
 * @property type The type of bonus.
 * @property expires The expiration date of the bonus.
 */
@JsonClass(generateAdapter = true)
data class MobileBonus(
    @Json(name = "Datos") val data: String,
    @Json(name = "Fecha inicio") val startDate: String,
    @Json(name = "tipo") val type: String,
    @Json(name = "Vence") val expires: String,
)
