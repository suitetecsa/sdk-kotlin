package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.InvalidSessionException

/**
 * Represents a user entity with associated client details, service information, status, and update times.
 *
 * @property client The client details associated with the user.
 * @property completed A status indicator for the user's completion state.
 * @property lastUpdate A timestamp indicating the last update time for the user's data.
 * @property services The collection of services linked to the user.
 * @property updatedServices A status or information about the user's updated services.
 */
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "cliente") val client: Client,
    @Json(name = "completado") val completed: String,
    @Json(name = "fechaActualizacion") val lastUpdate: String,
    @Json(name = "Servicios") val services: Services,
    @Json(name = "servicios_actualizados") val updatedServices: String,
)

/**
 * A custom Moshi JsonAdapter for deserializing JSON into either a `String` or a `User` object.
 *
 * This adapter determines how to parse incoming JSON data based on the structure of the data:
 * - If the JSON represents a `String`, the adapter will directly return the string value.
 * - If the JSON represents an object structure, the adapter will attempt to deserialize it
 *   into a `User` object using a `User` class adapter.
 *
 * This adapter does not support serialization (converting objects back into JSON).
 *
 * Behavior:
 * - When the JSON token is a `STRING`, the adapter invokes `nextString()` to fetch the string value.
 * - When the JSON token is a `BEGIN_OBJECT`, the adapter utilizes Moshi to construct a `User` object
 *   by integrating specific adapters for `Client` and `Services` types.
 * - If the JSON token is neither a `STRING` nor a `BEGIN_OBJECT`, a `JsonDataException` is thrown.
 *
 * This implementation is tailored for scenarios where a JSON response may dynamically
 * alternate between differing data representations, such as strings and objects.
 *
 * @throws JsonDataException if the JSON token type is unexpected.
 * @throws InvalidSessionException if the deserialization process encounters invalid user data.
 */
class UserAdapter : JsonAdapter<Any>() {
    override fun fromJson(reader: JsonReader): Any {
        // Verificar si el próximo token es un STRING o BEGIN_OBJECT
        return when (reader.peek()) {
            JsonReader.Token.STRING -> {
                // Leer y devolver el string (será vacío según tu descripción)
                reader.nextString()
            }
            JsonReader.Token.BEGIN_OBJECT -> {
                // Aquí asumimos que tienes una clase definida para el usuario, como `UserClass`.
                // Utiliza Moshi para deserializar el objeto usuario.
                val userAdapter = Moshi.Builder()
                    .add(Client::class.java, ClientAdapter())
                    .add(Services::class.java, ServicesAdapter())
                    .build()
                    .adapter(User::class.java)
                try {
                    userAdapter.fromJson(reader)!!
                } catch (e: JsonDataException) {
                    println("An error occurred :: ${e.message}")
                    throw InvalidSessionException("")
                }
            }
            else -> throw JsonDataException("Unexpected token: ${reader.peek()}")
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
