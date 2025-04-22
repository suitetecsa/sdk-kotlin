package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class ValidateCodeIdentityRequest(val dni: String, val confirmCode: String)

/**
 * A custom JsonAdapter for serializing `ValidateCodeIdentityRequest` into a JSON format
 * compatible with a specific API expectation.
 *
 * This adapter transforms a `ValidateCodeIdentityRequest` instance into a serialized JSON
 * representation containing a list of `NautaActionRequestParam` objects with specific keys
 * and values. The keys correspond to the fields expected by the API, such as "identidad"
 * and "codigoActivacion".
 *
 * The `fromJson` method is not supported and will throw an `UnsupportedOperationException`
 * if invoked.
 *
 * The `toJson` method converts the `ValidateCodeIdentityRequest` instance into a specified JSON
 * structure using the `ValidateCodeIdentityRequestJson` model and writes it to the provided
 * `JsonWriter`.
 */
internal class ValidateCodeIdentityRequestAdapter : JsonAdapter<ValidateCodeIdentityRequest>() {
    override fun fromJson(p0: JsonReader): ValidateCodeIdentityRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateCodeIdentityRequest?) {
        request?.let {
            val requestJson = ValidateCodeIdentityRequestJson(
                listOf(
                    NautaActionRequestParam("identidad", it.dni),
                    NautaActionRequestParam("codigoActivacion", it.confirmCode)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

/**
 * Represents a request model used to validate the identity of a code.
 *
 * This data class encapsulates a list of parameters required for a Nauta service
 * operation. Each parameter, represented as a `NautaActionRequestParam`, consists
 * of key-value pairs that are essential for this specific validation request.
 *
 * Typically, this class is utilized in scenarios where JSON serialization is needed
 * to send the validation request to an API.
 *
 * @property param A list of `NautaActionRequestParam` objects containing the necessary
 *                 parameters for the validation request.
 */
private data class ValidateCodeIdentityRequestJson(val param: List<NautaActionRequestParam>)
