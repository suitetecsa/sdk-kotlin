package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

/**
 * Represents a navigation service with details about operations, a navigation profile, and the type of associated
 * product.
 *
 * This data class models the structure of a navigation service and its related components:
 * - A list of `Operation` objects that represent the available operations within the service.
 * - A `NavProfile` object that details user account information and preferences.
 * - A string indicating the type of product related to the navigation service.
 *
 * Primary use cases of this class include navigation-related workflows and representing service details for users.
 *
 * @property operations A list of operations available in the navigation service.
 * @property profile A user's navigation profile containing account and access details.
 * @property productType The type of product associated with this navigation service.
 */
data class NavService(
    @Json(name = "operaciones") val operations: List<Operation>,
    @Json(name = "perfil") val profile: NavProfile,
    @Json(name = "tipoProducto") val productType: String,
)

/**
 * Custom JSON adapter for parsing `NavService` objects from JSON using the Moshi library.
 *
 * This adapter processes the JSON structure and maps it to the `NavService` data class,
 * handling nested objects such as `Operation` and `NavProfile`.
 *
 * Features:
 * - Parses the JSON fields "operaciones", "perfil", and "tipoProducto".
 * - Handles deserialization of nested `Operation` objects using a custom `OperationAdapter`.
 * - Throws a `JsonDataException` if any required fields are missing in the JSON payload.
 *
 * Not supported:
 * - This adapter does not implement serialization to JSON (`toJson` is unsupported).
 */
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
