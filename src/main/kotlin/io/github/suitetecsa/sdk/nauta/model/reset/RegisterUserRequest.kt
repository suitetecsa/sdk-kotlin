package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * Represents a request to register a user with the necessary information.
 *
 * @property captchaCode The code generated to verify the user is not a robot.
 * @property idRequest An identifier for the specific registration request.
 * @property dni The national identification number of the user.
 * @property phoneNumber The phone number of the user being registered.
 */
data class RegisterUserRequest(
    val captchaCode: String,
    val idRequest: String,
    val dni: String,
    val phoneNumber: String
)

/**
 * A custom JsonAdapter for serializing `RegisterUserRequest` objects to a specific JSON format.
 *
 * This adapter is responsible for transforming a `RegisterUserRequest` instance into a JSON
 * string that includes a structured representation of the request. The generated JSON object
 * includes the captcha code, request identifier, and a list of parameters required
 * for the registration process.
 *
 * The `toJson` method performs the serialization process by:
 * - Constructing a `RegisterUserRequestJson` object containing the necessary data.
 * - Adding predefined parameters ("via", "noIdentidad", "servicio") with values derived
 *   from the `RegisterUserRequest` properties.
 * - Writing the serialized JSON string to the provided `JsonWriter`.
 *
 * The `fromJson` method is not implemented and will throw an `UnsupportedOperationException`
 * if invoked, as deserialization is not supported by this adapter.
 */
internal class RegisterUserRequestAdapter : JsonAdapter<RegisterUserRequest>() {
    override fun fromJson(p0: JsonReader): RegisterUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: RegisterUserRequest?) {
        request?.let {
            val requestJson = RegisterUserRequestJson(
                it.captchaCode,
                it.idRequest,
                listOf(
                    NautaActionRequestParam("via", "SERVICIO_MOVIL"),
                    NautaActionRequestParam("noIdentidad", it.dni),
                    NautaActionRequestParam("servicio", it.phoneNumber)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

/**
 * Represents the JSON structure for a request to register a user in the Nauta system.
 *
 * This data class is used as part of the serialization process when creating a request
 * to register a user, including information such as a captcha code, request identifier,
 * and a list of parameters required for the operation.
 *
 * @property captchaCode The captcha text that the user must provide for validation.
 * @property idRequest A unique identifier for the request.
 * @property param A list of key-value pairs representing additional parameters required for the request.
 */
private data class RegisterUserRequestJson(
    @SerializedName("captchatext") val captchaCode: String,
    val idRequest: String,
    val param: List<NautaActionRequestParam>
)
