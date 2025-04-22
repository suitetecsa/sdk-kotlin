package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the JSON structure of a response containing user-related information.
 *
 * This data class is annotated with `@JsonClass` and `@Json` to support Moshi serialization and deserialization.
 * It acts as an intermediate mapping for JSON data into a `UsersResponse` object.
 *
 * @property resp The main response data, represented as a `UsersResponse` object and serialized under the JSON key
 * "resp".
 */
@JsonClass(generateAdapter = true)
data class UsersResponseJson(
    @Json(name = "resp") val resp: UsersResponse
)
