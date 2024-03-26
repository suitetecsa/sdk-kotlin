package io.github.suitetecsa.sdk.nauta.model.captcha

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CaptchaResponse(
    @Json(name = "text") val idRequest: String,
    val data: String
)
