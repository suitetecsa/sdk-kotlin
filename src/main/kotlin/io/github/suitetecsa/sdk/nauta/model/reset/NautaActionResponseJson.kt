package io.github.suitetecsa.sdk.nauta.model.reset

import com.squareup.moshi.JsonClass

/**
 * Represents a JSON response structure for a Nauta action.
 *
 * This data class serves as a container for the `data` field, which encapsulates
 * the details and result of a Nauta operation. It is primarily used for parsing
 * JSON responses with Moshi, where the main payload is structured in a nested format.
 *
 * @property data An instance of `NautaActionResponse` that contains the details
 * and result of the Nauta operation.
 */
@JsonClass(generateAdapter = true)
internal data class NautaActionResponseJson(val data: NautaActionResponse)
