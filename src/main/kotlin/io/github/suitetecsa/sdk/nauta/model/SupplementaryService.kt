package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a supplementary service with its name and associated value.
 *
 * This data class is used to model additional services with their corresponding descriptions in a system,
 * where the `service` property specifies the name or type of service and the `value` property describes
 * specific details or characteristics of the service.
 *
 * @property service The name or type of the supplementary service.
 * @property value The value or description associated with the supplementary service.
 */
@JsonClass(generateAdapter = true)
data class SupplementaryService(
    @Json(name = "Servicio") val service: String,
    @Json(name = "Valor") val value: String,
)
