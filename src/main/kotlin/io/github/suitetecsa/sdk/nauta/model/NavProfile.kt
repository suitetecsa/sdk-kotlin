package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a user's navigation profile with detailed account and session-related information.
 *
 * This data class is used to model key attributes of a user's profile, such as their account status,
 * session-related dates, bonus allocation, currency, and balance. Each property is annotated for proper
 * JSON serialization and deserialization.
 *
 * @property id A unique identifier for the navigation profile.
 * @property bonusToEnjoy A description of available bonuses for usage.
 * @property accessAccount The associated account name used for navigation access.
 * @property status The current status of the profile (e.g., active, inactive).
 * @property lockDate The date when the account was locked.
 * @property deletionDate The date when the account was deleted, if applicable.
 * @property saleDate The date when the account was sold.
 * @property bonusHours The number of hours awarded as a bonus.
 * @property currency The currency used for payments or balance calculations.
 * @property balance The current account balance.
 * @property accessType The type of account access (e.g., public, private).
 */
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
