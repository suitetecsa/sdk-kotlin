package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

/**
 * Represents the mail service data structure.
 *
 * This class is a data model used for mapping JSON objects related to a mail service.
 * It includes details about operations available in the service, the associated mail profile,
 * and the type of product being referred to.
 *
 * @property operations A list of operations supported by the mail service. Each operation contains details
 * such as its method, mode, and parameters required.
 * @property profile The profile information related to the mail service. This includes details such as
 * the associated email account, sale date, and currency used.
 * @property productType The type of product related to the mail service.
 */
data class MailService(
    @Json(name = "operaciones") val operations: List<Operation>,
    @Json(name = "perfil") val profile: MailProfile,
    @Json(name = "tipoProducto") val productType: String
)

/**
 * A custom JsonAdapter for deserializing JSON data to the `MailService` model.
 *
 * This adapter is responsible for parsing JSON objects containing information about mail services,
 * including operations, profile details, and product types.
 *
 * The adapter defines specific behavior for reading and parsing the following fields:
 * - 'operaciones': A list of operations, deserialized into `Operation` objects using a custom `OperationAdapter`.
 * - 'perfil': Profile details, deserialized as a `MailProfile` object.
 * - 'tipoProducto': The type of product, represented as a `String`.
 *
 * Unsupported operations:
 * - Writing (serialization) JSON data (`toJson` method is not supported and will throw an exception).
 *
 * Behavior:
 * - If any of the required fields (`operaciones`, `perfil`, or `tipoProducto`) are missing in the JSON data,
 *   the adapter will throw a `JsonDataException`.
 *
 * Constructs used:
 * - `JsonReader.Options` for optimizing field name matching during deserialization.
 * - Moshi library for handling JSON parsing.
 *
 * Note:
 * The adapter is specifically designed for reading JSON input and creating `MailService` objects.
 */
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
