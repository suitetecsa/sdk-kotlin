package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a request to log in to the Nauta service.
 *
 * This data class is used for sending login credentials to the server in order to authenticate
 * the user. It includes the following fields:
 *
 * @property username The username of the account to log in.
 * @property password The password associated with the username.
 * @property accountType The type of account being accessed, defaulting to "USUARIO_PORTAL".
 * @property idRequest A unique identifier for the login request.
 * @property captchaCode A CAPTCHA code to validate the request.
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String,
    @Json(name = "tipoCuenta") val accountType: String = "USUARIO_PORTAL",
    val idRequest: String,
    @Json(name = "captchatext") val captchaCode: String
)
