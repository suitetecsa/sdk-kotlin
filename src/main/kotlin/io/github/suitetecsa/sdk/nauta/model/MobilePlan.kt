package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a mobile plan with associated details.
 *
 * @property data The data allowance included in the mobile plan.
 * @property type The type or category of the mobile plan.
 * @property expires The expiration date of the mobile plan.
 */
@JsonClass(generateAdapter = true)
data class MobilePlan(
    @Json(name = "Datos") val data: String,
    @Json(name = "tipo") val type: String,
    @Json(name = "Vence") val expires: String,
)
