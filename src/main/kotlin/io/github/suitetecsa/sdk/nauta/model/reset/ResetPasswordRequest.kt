package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class ResetPasswordRequest(val account: String, val password: String, val confirmCode: String)

internal class ResetPasswordRequestAdapter : JsonAdapter<ResetPasswordRequest>() {
    override fun fromJson(p0: JsonReader): ResetPasswordRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ResetPasswordRequest?) {
        request?.let {
            val requestJson = ResetPasswordRequestJson(listOf(
                NautaActionRequestParam("cuenta", it.account),
                NautaActionRequestParam("password", it.password),
                NautaActionRequestParam("codigoActivacion", it.confirmCode)
            ))
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class ResetPasswordRequestJson(val param: List<NautaActionRequestParam>)
