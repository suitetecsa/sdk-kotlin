package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the structure of a JSON response containing login-related information.
 *
 * This data class is used to map the structure of server responses received after
 * a login attempt. The server response includes a nested object represented by the
 * `resp` property, which corresponds to an instance of `LoginResponse`.
 *
 * The `LoginResponseJson` class facilitates deserialization of the JSON payload into
 * Kotlin objects, enabling access to the contained `LoginResponse` data for further
 * processing or validation.
 *
 * An internal class intended for use within the SDK, making it unavailable for external access.
 */
@JsonClass(generateAdapter = true)
internal data class LoginResponseJson(
    @Json(name = "resp") val resp: LoginResponse
)
