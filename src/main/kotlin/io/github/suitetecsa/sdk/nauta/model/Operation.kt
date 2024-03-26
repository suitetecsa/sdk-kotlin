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
