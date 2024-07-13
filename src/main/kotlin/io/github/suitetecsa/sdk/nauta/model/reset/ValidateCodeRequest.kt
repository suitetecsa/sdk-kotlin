package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class ValidateCodeRequest(val portalUser: String, val confirmCode: String)

class ValidateCodeRequestAdapter : JsonAdapter<ValidateCodeRequest>() {
    override fun fromJson(p0: JsonReader): ValidateCodeRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateCodeRequest?) {
        request?.let {
            val requestJson = ValidateCodeRequestJson(listOf(
                NautaActionRequestParam("usuarioPortal", it.portalUser),
                NautaActionRequestParam("codigoActivacion", it.confirmCode),
            ))
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class ValidateCodeRequestJson(val param: List<NautaActionRequestParam>)
