package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

data class RegisterUserRequest(
    val captchaCode: String,
    val idRequest: String,
    val dni: String,
    val phoneNumber: String
)

internal class RegisterUserRequestAdapter : JsonAdapter<RegisterUserRequest>() {
    override fun fromJson(p0: JsonReader): RegisterUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: RegisterUserRequest?) {
        request?.let {
            val requestJson = RegisterUserRequestJson(
                it.captchaCode,
                it.idRequest,
                listOf(
                    NautaActionRequestParam("via", "SERVICIO_MOVIL"),
                    NautaActionRequestParam("noIdentidad", it.dni),
                    NautaActionRequestParam("servicio", it.phoneNumber)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class RegisterUserRequestJson(
    @SerializedName("captchatext") val captchaCode: String,
    val idRequest: String,
    val param: List<NautaActionRequestParam>
)
