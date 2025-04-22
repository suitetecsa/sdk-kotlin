package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a profile for fixed telephony, containing essential details such as an identifier,
 * associated lists (e.g., supplementary services), and a phone number.
 *
 * @property id Unique identifier for the fixed telephony profile.
 * @property lists An optional object containing telephony-related lists, such as supplementary services.
 * @property phoneNumber The phone number associated with the fixed telephony profile.
 */
@JsonClass(generateAdapter = true)
data class FixedTelephonyProfile(
    val id: String,
    @Json(name = "Listas") val lists: FixedTelephonyLists?,
    @Json(name = "Número de teléfono") val phoneNumber: String,
)
