package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

data class FixedTelephonyLists(
    val supplementaryServices: List<SupplementaryService>,
)

class FixedTelephonyListsAdapter : JsonAdapter<FixedTelephonyLists>() {
    private val options: JsonReader.Options = JsonReader.Options.of("Servicios suplementarios")
    private val moshi: Moshi = Moshi.Builder().build()
    private val serviceJsonAdapter = moshi.adapter(SupplementaryService::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): FixedTelephonyLists {
        val supplementaryServices = mutableListOf<SupplementaryService>()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val service = serviceJsonAdapter.fromJson(reader)
                        service?.let { supplementaryServices.add(it) }
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return FixedTelephonyLists(supplementaryServices)
    }

    override fun toJson(p0: JsonWriter, p1: FixedTelephonyLists?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
