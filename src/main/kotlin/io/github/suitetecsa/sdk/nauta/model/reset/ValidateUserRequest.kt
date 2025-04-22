package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * Represents a request to validate a user based on the provided parameters.
 *
 * This data class encapsulates the necessary fields required to submit a
 * user validation request within a specific system or service, such as
 * verifying user identity based on a captcha code, request ID, and
 * associated portal user.
 *
 * @property captchaCode The captcha code provided by the user for verification purposes.
 * @property idRequest The identifier of the validation request, used for tracking or logging.
 * @property portalUser The identifier or username of the portal user being validated.
 */
data class ValidateUserRequest(
    val captchaCode: String,
    val idRequest: String,
    val portalUser: String
)

/**
 * A custom JsonAdapter for serializing the `ValidateUserRequest` into a specific
 * JSON format required by the Nauta API.
 *
 * This adapter transforms a `ValidateUserRequest` instance into a JSON output
 * that includes the captcha code, request ID, and other necessary parameters
 * encapsulated in a list of `NautaActionRequestParam` objects. The serialization
 * ensures compatibility with the Nauta system's requirements.
 *
 * The `toJson` method serializes the `ValidateUserRequest` instance into JSON.
 * If the `ValidateUserRequest` is null, the writer outputs a `null` value.
 *
 * The `fromJson` method is not supported and will throw an `UnsupportedOperationException`
 * if invoked.
 */
class ValidateUserRequestAdapter : JsonAdapter<ValidateUserRequest>() {
    override fun fromJson(p0: JsonReader): ValidateUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateUserRequest?) {
        request?.let {
            val requestJson = ValidateUserRequestJson(
                it.captchaCode,
                it.idRequest,
                listOf(NautaActionRequestParam("usuarioPortal", it.portalUser))
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

/**
 * Represents the JSON structure for a request to validate a user.
 *
 * This data class is used to encapsulate the necessary parameters for validating
 * a user in the Nauta system. It includes the captcha text, a unique request
 * identifier, and a list of action parameters required for the validation process.
 *
 * @property captchaText The text of the captcha to be validated.
 * @property idRequest A unique identifier for the validation request.
 * @property param A list of key-value pairs representing parameters needed for the request.
 */
private data class ValidateUserRequestJson(
    @SerializedName("captchatext") val captchaText: String,
    val idRequest: String,
    val param: List<NautaActionRequestParam>
)
