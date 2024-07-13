package io.github.suitetecsa.sdk.nauta.model.reset

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.github.suitetecsa.sdk.exception.NautaAttributeException

private const val DETAIL_INDEX = 0
private const val RESULT_INDEX = 1

data class NautaActionResponse(
    @Json(name = "detalle") val detail: String? = null,
    @Json(name = "resultado") val result: String
)

class NautaActionResponseAdapter : JsonAdapter<NautaActionResponse>() {
    override fun fromJson(reader: JsonReader): NautaActionResponse {
        val nautaActionResponseAdapterExtra = Moshi.Builder()
            .add(NautaActionResponse::class.java, NautaActionResponseAdapterExtra())
            .build()
            .adapter(NautaActionResponseJson::class.java)
        val json = nautaActionResponseAdapterExtra.fromJson(reader)
        return json?.let {
            NautaActionResponse(it.data.detail, it.data.result)
        } ?: throw NautaAttributeException("El cuerpo del JSON no puede ser nulo")
    }

    override fun toJson(p0: JsonWriter, p1: NautaActionResponse?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}

internal class NautaActionResponseAdapterExtra : JsonAdapter<NautaActionResponse>() {
    private val options: JsonReader.Options = JsonReader.Options.of(
        "detalle",
        "resultado"
    )
    override fun fromJson(reader: JsonReader): NautaActionResponse {
        return when (reader.peek()) {
            JsonReader.Token.STRING -> {
                NautaActionResponse(result = reader.nextString())
            }
            JsonReader.Token.BEGIN_OBJECT -> {
                var detail: String? = null
                var result: String? = null

                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.selectName(options)) {
                        DETAIL_INDEX -> detail = reader.nextString()
                        RESULT_INDEX -> result = reader.nextString()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                NautaActionResponse(detail, result!!)
            }
            else -> throw JsonDataException("Unexpected token: ${reader.peek()}")
        }
    }

    override fun toJson(p0: JsonWriter, p1: NautaActionResponse?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
