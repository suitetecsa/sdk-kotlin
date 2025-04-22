package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the profile of an email account used for tracking sales-related information.
 *
 * This class is annotated with Moshi's `@JsonClass` to enable JSON serialization and deserialization.
 * Each property is mapped to a specific JSON key via the `@Json` annotation.
 *
 * @property id A unique identifier for the profile.
 * @property emailAccount The email account associated with the profile, mapped to "Cuenta de correo" in the JSON.
 * @property saleDate The date of the sale, represented in string format, mapped to "Fecha de venta" in the JSON.
 * @property currency The currency in which the sale was made, mapped to "Moneda" in the JSON.
 */
@JsonClass(generateAdapter = true)
data class MailProfile(
    val id: String,
    @Json(name = "Cuenta de correo") val emailAccount: String,
    @Json(name = "Fecha de venta") val saleDate: String,
    @Json(name = "Moneda") val currency: String
)
