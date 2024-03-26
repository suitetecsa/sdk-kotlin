package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "cliente") val client: Client,
    @Json(name = "completado") val completed: String,
    @Json(name = "fechaActualizacion") val lastUpdate: String,
    @Json(name = "Servicios") val services: Services,
    @Json(name = "servicios_actualizados") val updatedServices: String,
)

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
                userAdapter.fromJson(reader)!!
            }
            else -> throw JsonDataException("Unexpected token: ${reader.peek()}")
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
