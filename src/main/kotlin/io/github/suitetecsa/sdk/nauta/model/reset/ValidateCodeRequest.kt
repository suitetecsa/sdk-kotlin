package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * Represents a request to validate a confirmation code for a portal user.
 *
 * This data class is used to encapsulate the necessary fields for validating
 * a confirmation code within the Nauta system.
 *
 * @property portalUser The identifier of the portal user for whom the validation is being performed.
 * @property confirmCode The confirmation code that will be validated.
 */
data class ValidateCodeRequest(val portalUser: String, val confirmCode: String)

/**
 * A custom JsonAdapter for serializing a `ValidateCodeRequest` object into
 * a specific JSON format compatible with a particular API requirement.
 *
 * This adapter transforms a `ValidateCodeRequest` instance into a JSON object
 * that contains a list of `NautaActionRequestParam` objects. Each `NautaActionRequestParam`
 * represents a key-value pair, structured according to API expectations.
 *
 * The `toJson` method serializes a `ValidateCodeRequest` into this custom JSON structure,
 * where:
 * - The key "usuarioPortal" corresponds to the `portalUser` property.
 * - The key "codigoActivacion" corresponds to the `confirmCode` property.
 *
 * If the provided `ValidateCodeRequest` is null, the JSON output will be null.
 *
 * The `fromJson` method is not implemented in this adapter and will throw an
 * `UnsupportedOperationException` if invoked.
 */
class ValidateCodeRequestAdapter : JsonAdapter<ValidateCodeRequest>() {
    override fun fromJson(p0: JsonReader): ValidateCodeRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateCodeRequest?) {
        request?.let {
            val requestJson = ValidateCodeRequestJson(
                listOf(
                    NautaActionRequestParam("usuarioPortal", it.portalUser),
                    NautaActionRequestParam("codigoActivacion", it.confirmCode),
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

/**
 * Represents a request for validating a specific code within the Nauta system.
 *
 * This data class encapsulates a list of parameters required to perform the validation.
 * Each parameter is represented as an instance of `NautaActionRequestParam`, containing
 * key-value pairs necessary for the operation.
 *
 * Typically used as part of JSON serialization to structure the request payload
 * for the respective Nauta API.
 *
 * @property param A list of key-value parameter pairs required for the code validation request.
 */
private data class ValidateCodeRequestJson(val param: List<NautaActionRequestParam>)
