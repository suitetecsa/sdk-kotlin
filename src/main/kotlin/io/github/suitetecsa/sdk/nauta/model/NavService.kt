package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

data class NavService(
    @Json(name = "operaciones") val operations: List<Operation>,
    @Json(name = "perfil") val profile: NavProfile,
    @Json(name = "tipoProducto") val productType: String,
)

class NavServiceAdapter : JsonAdapter<NavService>() {
    private val options: JsonReader.Options = JsonReader.Options.of("operaciones", "perfil", "tipoProducto")
    private val moshi: Moshi = Moshi.Builder()
        .add(Operation::class.java, OperationAdapter())
        .build()
    private val operationAdapter: JsonAdapter<Operation> = moshi.adapter(Operation::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): NavService {
        var operations: List<Operation>? = null
        var profile: NavProfile? = null
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
                    profile = moshi.adapter(NavProfile::class.java).fromJson(reader)
                }
                2 -> { // "tipoProducto"
                    productType = reader.nextString()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (operations != null && profile != null && productType != null) {
            return NavService(
                operations = operations,
                profile = profile,
                productType = productType
            )
        } else {
            throw JsonDataException("Missing required fields")
        }
    }

    override fun toJson(p0: JsonWriter, p1: NavService?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
