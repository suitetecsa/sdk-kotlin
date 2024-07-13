package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class ValidateCodeIdentityRequest(val dni: String, val confirmCode: String)

internal class ValidateCodeIdentityRequestAdapter : JsonAdapter<ValidateCodeIdentityRequest>() {
    override fun fromJson(p0: JsonReader): ValidateCodeIdentityRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateCodeIdentityRequest?) {
        request?.let {
            val requestJson = ValidateCodeIdentityRequestJson(
                listOf(
                    NautaActionRequestParam("identidad", it.dni),
                    NautaActionRequestParam("codigoActivacion", it.confirmCode)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class ValidateCodeIdentityRequestJson(val param: List<NautaActionRequestParam>)
