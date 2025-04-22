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

/**
 * Represents the response from a Nauta action.
 *
 * This data class encapsulates the result of a Nauta operation, including an optional
 * detail message and the operation result. The result property is mandatory, while
 * the detail property provides additional information when available.
 *
 * @property detail An optional field containing additional details about the Nauta action.
 * @property result The outcome or result of the Nauta action, represented as a string.
 */
data class NautaActionResponse(
    @Json(name = "detalle") val detail: String? = null,
    @Json(name = "resultado") val result: String
)

/**
 * A custom adapter for serializing and deserializing `NautaActionResponse` objects
 * with Moshi. This adapter maps the JSON response structure into the `NautaActionResponse`
 * model.
 *
 * The `fromJson` method parses the JSON into a specialized intermediary object
 * (`NautaActionResponseJson`) using a nested adapter (`NautaActionResponseAdapterExtra`)
 * and transforms it into a `NautaActionResponse` instance. If the JSON body is null,
 * a `NautaAttributeException` is thrown.
 *
 * The `toJson` method is not supported and will throw an `UnsupportedOperationException`
 * if invoked.
 *
 * This class is designed to handle responses where the main data is encapsulated
 * within a `data` object containing `detalle` (detail) and `resultado` (result).
 */
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

/**
 * A custom JsonAdapter for serializing and deserializing `NautaActionResponse` objects.
 *
 * This adapter supports parsing `NautaActionResponse` from JSON strings or JSON objects.
 * - If the JSON token is a string, it treats the string as the "result" property.
 * - If the JSON token is an object, it reads the `detalle` and `resultado` fields,
 *   constructing a `NautaActionResponse` instance.
 *
 * Unsupported JSON tokens result in a `JsonDataException`.
 *
 * Serialization (toJson) is not implemented and will throw an `UnsupportedOperationException`
 * if attempted.
 *
 * The `detalle` and `resultado` fields correspond to the `detail` and `result`
 * properties in the `NautaActionResponse` model, respectively.
 */
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
