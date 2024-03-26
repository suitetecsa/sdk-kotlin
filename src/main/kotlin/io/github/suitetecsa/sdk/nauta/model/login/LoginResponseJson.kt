package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class LoginResponseJson(
    @Json(name = "resp") val resp: LoginResponse
)
