package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

data class MailService(
    @Json(name = "operaciones") val operations: List<Operation>,
    @Json(name = "perfil") val profile: MailProfile,
    @Json(name = "tipoProducto") val productType: String
)

class MailServiceAdapter : JsonAdapter<MailService>() {
    private val options: JsonReader.Options = JsonReader.Options.of("operaciones", "perfil", "tipoProducto")

    // Este adaptador se utiliza para convertir cada valor del mapa de operaciones en un objeto Operation
    private val moshi: Moshi = Moshi.Builder()
        .add(Operation::class.java, OperationAdapter())
        .build()
    private val operationAdapter: JsonAdapter<Operation> = moshi.adapter(Operation::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): MailService {
        var operations: List<Operation>? = null
        var profile: MailProfile? = null
        var productType: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> { // "operaciones"
                    val operationsList = mutableListOf<Operation>()
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName() // Ignora el nombre de la clave, ya que no lo necesitamos para la lista
                        val operation = operationAdapter.fromJson(reader)
                        operation?.let { operationsList.add(it) }
                    }
                    reader.endObject()
                    operations = operationsList
                }
                1 -> { // "perfil"
                    profile = moshi.adapter(MailProfile::class.java).fromJson(reader)
                }
                2 -> { // "tipoProducto"
                    productType = reader.nextString()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (operations != null && profile != null && productType != null) {
            return MailService(
                operations = operations,
                profile = profile,
                productType = productType
            )
        } else {
            throw JsonDataException("Missing required fields")
        }
    }

    override fun toJson(writer: JsonWriter, value: MailService?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
