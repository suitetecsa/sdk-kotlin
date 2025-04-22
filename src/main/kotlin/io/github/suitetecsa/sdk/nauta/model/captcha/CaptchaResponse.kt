package io.github.suitetecsa.sdk.nauta.model.captcha

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from a CAPTCHA-related request.
 *
 * This class is used for managing the structured data received from a CAPTCHA response.
 * It includes an identifier for the request and the associated data or result of the CAPTCHA operation.
 *
 * @property idRequest The unique identifier for the CAPTCHA request, mapped from the JSON field "text".
 * @property data The data or result associated with the CAPTCHA request.
 */
@JsonClass(generateAdapter = true)
data class CaptchaResponse(
    @Json(name = "text") val idRequest: String,
    val data: String
)
