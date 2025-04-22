package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a request object for managing user-related data.
 *
 * @property email The email address of the user.
 * @property lastUpdate The last update timestamp for the user, serialized as "ultimaActualizacion".
 */
@JsonClass(generateAdapter = true)
data class UsersRequest(
    val email: String,
    @Json(name = "ultimaActualizacion") val lastUpdate: String,
)
