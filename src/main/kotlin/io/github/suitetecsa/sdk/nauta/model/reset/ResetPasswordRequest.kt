package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * Represents a request to reset a user's password.
 *
 * This data class encapsulates the necessary information required to initiate a password reset
 * operation for a user account. The included fields are:
 *
 * @property account The identifier for the user account associated with the password reset.
 * @property password The new password to be set for the user account.
 * @property confirmCode The confirmation code required to authorize the password reset operation.
 */
data class ResetPasswordRequest(val account: String, val password: String, val confirmCode: String)

/**
 * A custom JsonAdapter for serializing `ResetPasswordRequest` objects into JSON format.
 *
 * This adapter is used to transform a `ResetPasswordRequest` instance into a JSON object
 * compatible with a specific API requirement. The resulting JSON object contains a list
 * of `NautaActionRequestParam` objects with specific keys and their corresponding values,
 * such as account, password, and confirmation code.
 *
 * This adapter does not support deserialization (fromJson). Any attempt to use the
 * `fromJson` method will throw an `UnsupportedOperationException`.
 */
internal class ResetPasswordRequestAdapter : JsonAdapter<ResetPasswordRequest>() {
    override fun fromJson(p0: JsonReader): ResetPasswordRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ResetPasswordRequest?) {
        request?.let {
            val requestJson = ResetPasswordRequestJson(
                listOf(
                    NautaActionRequestParam("cuenta", it.account),
                    NautaActionRequestParam("password", it.password),
                    NautaActionRequestParam("codigoActivacion", it.confirmCode)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

/**
 * Represents a request to reset a password via the Nauta service.
 *
 * This data class encapsulates the parameters required for a reset password
 * operation in the form of a list of `NautaActionRequestParam` objects.
 *
 * The parameters typically include key-value pairs necessary for the Nauta service
 * to process the password reset request.
 */
private data class ResetPasswordRequestJson(val param: List<NautaActionRequestParam>)
