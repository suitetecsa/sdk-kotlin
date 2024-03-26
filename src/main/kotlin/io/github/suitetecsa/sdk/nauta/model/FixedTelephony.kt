package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

data class FixedTelephony(
    val operations: List<Operation>,
    val profile: FixedTelephonyProfile,
)

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