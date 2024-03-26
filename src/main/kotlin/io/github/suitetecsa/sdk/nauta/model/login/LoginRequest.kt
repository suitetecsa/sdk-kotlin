package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String,
    @Json(name = "tipoCuenta") val accountType: String = "USUARIO_PORTAL",
    val idRequest: String,
    @Json(name = "captchatext") val captchaCode: String
)
