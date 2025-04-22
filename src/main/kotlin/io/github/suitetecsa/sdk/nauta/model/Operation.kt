package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

private const val ALERT_INDEX = 0
private const val E_COMMERCE_INDEX = 1
private const val ID_INDEX = 2
private const val METHOD_INDEX = 3
private const val MODE_INDEX = 4
private const val OPERATION_INDEX = 5
private const val PARAMETERS_INDEX = 6
private const val TYPE_INDEX = 7
private const val URL_INDEX = 8

/**
 * Represents an operation that can be performed within the system.
 *
 * @property alterServiceProfile Defines the service profile to be altered by the operation.
 * @property eCommerce Represents the e-commerce context or platform related to the operation.
 * @property id Identifies the unique ID of the operation.
 * @property method Indicates the HTTP method (e.g., GET, POST) used for the operation.
 * @property mode Specifies the operational mode.
 * @property operation Describes the name or type of the operation being performed.
 * @property parameters A list of parameters required or involved in the operation.
 * @property type Represents the type of operation as defined by the OperationType enum.
 * @property url Specifies the URL endpoint associated with the operation.
 */
data class Operation(
    val alterServiceProfile: String,
    val eCommerce: String,
    val id: String,
    val method: String,
    val mode: String,
    val operation: String,
    val parameters: List<Parameter>,
    val type: OperationType,
    val url: String,
)

/**
 * A custom adapter for serializing and deserializing `Operation` objects with Moshi.
 *
 * The `OperationAdapter` is designed to parse JSON data into an `Operation` object by handling
 * complex structures, nested fields, and custom data mappings. It uses Moshi to facilitate
 * this process, leveraging specific adapters for nested types such as `Parameter` and `OperationType`.
 *
 * The implemented functionality includes:
 *
 * - Deserialization (`fromJson`): Converts JSON data into an `Operation` object by mapping predefined
 *   JSON field names to `Operation` properties and resolving nested objects through their
 *   respective adapters.
 * - Ignoring unknown or unused JSON fields to avoid potential mapping issues.
 * - Assigning default values to certain properties when they are not present or parsed from the JSON.
 *
 * Serialization (`toJson`) is not supported and will throw an `UnsupportedOperationException` if attempted.
 *
 * Main JSON fields processed by this adapter include:
 * - "alteraPerfilServicio": Represents the `alterServiceProfile` field.
 * - "comercioElectronico": Represents the `eCommerce` field.
 * - "id": Represents the `id` field.
 * - "metodo": Represents the `method` field.
 * - "modo": Represents the `mode` field.
 * - "operacion": Represents the `operation` field.
 * - "parametros": Represents the `parameters` field as a list of `Parameter` objects.
 * - "tipo": Represents the `type` field as an `OperationType` enumeration.
 * - "url": Represents the `url` field.
 *
 * The adapter ensures robust handling of missing, null, or unexpected data in the JSON structure.
 *
 * Known limitations:
 * - Serialization is not implemented.
 * - Specific constraints and assumptions, such as assigning default values for the `OperationType`
 *   or non-nullable fields in the `Operation` model, are embedded in this adapter’s implementation.
 */
class OperationAdapter : JsonAdapter<Operation>() {
    private val options: JsonReader.Options = JsonReader.Options.of(
        "alteraPerfilServicio", "comercioElectronico", "id", "metodo", "modo", "operacion", "parametros", "tipo", "url"
    )
    private val moshi: Moshi = Moshi.Builder().build()
    private val parametersAdapter: JsonAdapter<Parameter> = moshi.adapter(Parameter::class.java)
    private val operationTypeAdapter: JsonAdapter<OperationType> = moshi.adapter(OperationType::class.java)

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    override fun fromJson(reader: JsonReader): Operation {
        var alterServiceProfile: String? = null
        var eCommerce: String? = null
        var id: String? = null
        var method: String? = null
        var mode: String? = null
        var operationString: String? = null
        var parameters: List<Parameter>? = null
        var type: OperationType? = null
        var url: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                ALERT_INDEX -> alterServiceProfile = reader.nextString()
                E_COMMERCE_INDEX -> eCommerce = reader.nextString()
                ID_INDEX -> id = reader.nextString()
                METHOD_INDEX -> method = reader.nextString()
                MODE_INDEX -> mode = reader.nextString()
                OPERATION_INDEX -> operationString = reader.nextString()
                PARAMETERS_INDEX -> {
                    val parametersList = mutableListOf<Parameter>()
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val parameter = parametersAdapter.fromJson(reader)
                        parameter?.let { parametersList.add(it) }
                    }
                    reader.endObject()
                    parameters = parametersList
                }
                TYPE_INDEX -> type = operationTypeAdapter.fromJson(reader)
                URL_INDEX -> url = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Operation(
            alterServiceProfile = alterServiceProfile ?: "",
            eCommerce = eCommerce ?: "",
            id = id ?: "",
            method = method ?: "",
            mode = mode ?: "",
            operation = operationString ?: "",
            parameters = parameters ?: emptyList(),
            type = type ?: OperationType.CONSULT, // Asume un valor predeterminado o maneja como necesario
            url = url ?: ""
        )
    }

    override fun toJson(writer: JsonWriter, value: Operation?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
