package io.github.suitetecsa.sdk.nauta.model.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.nauta.model.UserAdapter

@JsonClass(generateAdapter = true)
data class UsersResponse(
    val user: Any,
    @Json(name = "resultado") val result: String,
)

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
