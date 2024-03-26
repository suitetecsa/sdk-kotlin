package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersRequest(
    val email: String,
    @Json(name = "ultimaActualizacion") val lastUpdate: String,
)
