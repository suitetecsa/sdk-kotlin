package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a parameter with specific attributes used for configuration or data transmission.
 *
 * @property name The name of the parameter.
 * @property order The order or sequence in which the parameter is used or processed.
 * @property parameter The key or identifier of the parameter.
 * @property type The data type of the parameter value.
 * @property value The value assigned to the parameter.
 */
@JsonClass(generateAdapter = true)
data class Parameter(
    @Json(name = "nombre") val name: String,
    @Json(name = "orden") val order: String,
    @Json(name = "parametro") val parameter: String,
    @Json(name = "tipo") val type: String,
    @Json(name = "valor") val value: String,
)
