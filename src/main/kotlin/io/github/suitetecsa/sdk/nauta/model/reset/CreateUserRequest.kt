package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class CreateUserRequest(val phoneNumber: String, val password: String, val dni: String)

internal class CreateUserRequestAdapter : JsonAdapter<CreateUserRequest>() {
    override fun fromJson(p0: JsonReader): CreateUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: CreateUserRequest?) {
        request?.let {
            val requestJson = CreateUserRequestJson(
                listOf(
                    NautaActionRequestParam("usuario", it.phoneNumber),
                    NautaActionRequestParam("password", it.password),
                    NautaActionRequestParam("noIdentidad", it.dni)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class CreateUserRequestJson(val param: List<NautaActionRequestParam>)
