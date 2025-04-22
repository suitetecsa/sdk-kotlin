package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.nauta.model.UserAdapter

/**
 * Represents the response data received after a successful login attempt to the Nauta service.
 *
 * This class is used to map the response received from the server after sending a login request.
 * It includes the following properties:
 *
 * @property token A unique token issued upon successful authentication.
 * @property user Information about the user, represented as a generic object.
 * @property result The outcome or result of the login process, typically indicating success or failure.
 */
@JsonClass(generateAdapter = true)
data class LoginResponse(
    val token: String,
    val user: Any,
    @Json(name = "resultado") val result: String,
)

/**
 * A custom adapter for deserializing JSON into LoginResponse objects using Moshi.
 *
 * This adapter is used to handle the specific structure of the JSON data related to login responses.
 * It deserializes a `LoginResponseJson` object, extracts its relevant fields, and maps them to a
 * `LoginResponse` instance.
 *
 * The adapter is compatible with custom serialization and deserialization logic for certain nested
 * objects like the `user` field, which relies on `UserAdapter` for handling its parsing.
 *
 * The `toJson` method is unsupported and will throw an exception if invoked.
 *
 * Exceptions:
 * - `NautaAttributeException`: Thrown if the response body (parsed JSON) is null.
 */
class LoginResponseAdapter : JsonAdapter<LoginResponse>() {
    override fun fromJson(reader: JsonReader): LoginResponse {
        // Obtén un adaptador para LoginResponseJson.class
        val loginResponseAdapter = Moshi.Builder()
            .add(Any::class.java, UserAdapter())
            .build()
            .adapter(LoginResponseJson::class.java)
        val json = loginResponseAdapter.fromJson(reader)
        // Asegúrate de que json no sea nulo, lanzando una excepción si lo es.
        return json?.let {
            LoginResponse(it.resp.token, it.resp.user, it.resp.result)
        } ?: throw NautaAttributeException("El cuerpo del JSON no puede ser nulo")
    }

    override fun toJson(p0: JsonWriter, p1: LoginResponse?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
