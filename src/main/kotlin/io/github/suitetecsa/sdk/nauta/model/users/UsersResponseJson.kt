package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersResponseJson(
    @Json(name = "resp") val resp: UsersResponse
)
