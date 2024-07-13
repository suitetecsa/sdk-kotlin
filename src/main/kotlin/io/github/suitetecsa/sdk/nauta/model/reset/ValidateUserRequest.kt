package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class ValidateUserRequest(
    val captchaCode: String,
    val idRequest: String,
    val portalUser: String
)

class ValidateUserRequestAdapter : JsonAdapter<ValidateUserRequest>() {
    override fun fromJson(p0: JsonReader): ValidateUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: ValidateUserRequest?) {
        request?.let {
            val requestJson = ValidateUserRequestJson(
                it.captchaCode,
                it.idRequest,
                listOf(NautaActionRequestParam("usuarioPortal", it.portalUser))
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class ValidateUserRequestJson(
    @SerializedName("captchatext") val captchaText: String,
    val idRequest: String,
    val param: List<NautaActionRequestParam>
)
