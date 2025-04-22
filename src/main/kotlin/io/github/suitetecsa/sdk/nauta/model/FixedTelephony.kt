package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

/**
 * Represents a fixed telephony service, encapsulating its operations and profile information.
 *
 * @property operations List of actions or processes associated with the fixed telephony service.
 * @property profile The configuration or detailed information about the fixed telephony service.
 */
data class FixedTelephony(
    val operations: List<Operation>,
    val profile: FixedTelephonyProfile,
)

/**
 * A custom JSON adapter for serializing and deserializing instances of the FixedTelephony class.
 * This adapter handles specific JSON structure and maps it to FixedTelephony,
 * including a list of operations and a profile.
 *
 * This adapter:
 * - Is built with Moshi, a JSON library for processing JSON into objects and vice versa.
 * - Utilizes custom adapters for mapping nested objects such as Operation and FixedTelephonyLists.
 * - Deserializes JSON objects into FixedTelephony instances by interpreting specified keys.
 *
 * Limitations:
 * - Serialization (toJson) is not supported and will throw UnsupportedOperationException.
 */
class FixedTelephonyAdapter : JsonAdapter<FixedTelephony>() {
    private val options: JsonReader.Options = JsonReader.Options.of("operaciones", "perfil")
    private val moshi: Moshi = Moshi.Builder()
        .add(Operation::class.java, OperationAdapter())
        .add(FixedTelephonyLists::class.java, FixedTelephonyListsAdapter())
        .build()
    private val operationJsonAdapter: JsonAdapter<Operation> = moshi.adapter(Operation::class.java)
    private val profileJsonAdapter: JsonAdapter<FixedTelephonyProfile> =
        moshi.adapter(FixedTelephonyProfile::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): FixedTelephony {
        val operations = mutableListOf<Operation>()
        var profile: FixedTelephonyProfile? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val operation = operationJsonAdapter.fromJson(reader)
                        operation?.let { operations.add(it) }
                    }
                    reader.endObject()
                }
                1 -> profile = profileJsonAdapter.fromJson(reader)
                else -> reader.skipName()
            }
        }
        reader.endObject()
        return FixedTelephony(operations, profile!!)
    }

    override fun toJson(p0: JsonWriter, p1: FixedTelephony?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
