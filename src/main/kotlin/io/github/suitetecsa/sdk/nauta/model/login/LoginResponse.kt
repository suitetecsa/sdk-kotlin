package io.github.suitetecsa.sdk.nauta.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.nauta.model.UserAdapter

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val token: String,
    val user: Any,
    @Json(name = "resultado") val result: String,
)

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
