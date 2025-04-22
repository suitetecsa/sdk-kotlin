package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.nauta.model.UserAdapter

/**
 * Represents the response object containing user-related information.
 *
 * This class is annotated with `@JsonClass` and `@Json` to support Moshi serialization and deserialization.
 *
 * @property user The user data contained in the response, represented as a generic object.
 * @property result A string value indicating the result status of the response, serialized as "resultado".
 */
@JsonClass(generateAdapter = true)
data class UsersResponse(
    val user: Any,
    @Json(name = "resultado") val result: String,
)

/**
 * A custom JsonAdapter for deserializing UsersResponse objects using Moshi.
 *
 * This adapter is responsible for converting JSON data into `UsersResponse` objects. It uses another
 * adapter to first parse the JSON into its intermediate representation (`UsersResponseJson`), and then
 * ensures the required fields are present before transforming it into the final `UsersResponse` object.
 *
 * Note:
 * - The `toJson` method in this adapter is not supported and will throw an UnsupportedOperationException if called.
 *
 * Exceptions:
 * - Throws `NautaAttributeException` if the JSON response body is null during deserialization.
 */
class UsersResponseAdapter : JsonAdapter<UsersResponse>() {
    override fun fromJson(reader: JsonReader): UsersResponse {
        // Obtén un adaptador para LoginResponseJson.class
        val usersResponseAdapter = Moshi.Builder()
            .add(Any::class.java, UserAdapter())
            .build()
            .adapter(UsersResponseJson::class.java)
        val json = usersResponseAdapter.fromJson(reader)
        // Asegúrate de que json no sea nulo, lanzando una excepción si lo es.
        json ?: throw NautaAttributeException("El cuerpo del JSON no puede ser nulo")
        return UsersResponse(json.resp.user, json.resp.result)
    }

    override fun toJson(p0: JsonWriter, p1: UsersResponse?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
