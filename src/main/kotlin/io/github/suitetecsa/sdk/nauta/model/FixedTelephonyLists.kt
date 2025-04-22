package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

/**
 * Represents a set of fixed telephony-related lists, specifically containing supplementary services.
 *
 * @property supplementaryServices A list of supplementary services associated with fixed telephony.
 */
data class FixedTelephonyLists(
    val supplementaryServices: List<SupplementaryService>,
)

/**
 * A custom adapter for the `FixedTelephonyLists` class that facilitates the parsing of JSON data
 * and the creation of `FixedTelephonyLists` instances using Moshi. This adapter specializes in
 * deserializing JSON into objects that represent fixed telephony lists, including information
 * about supplementary services.
 *
 * This class overrides the `fromJson` method to parse JSON and populate a `FixedTelephonyLists`
 * instance. The `toJson` method is not supported and will throw an `UnsupportedOperationException`
 * if called.
 *
 * Primary responsibilities:
 * - Parse and deserialize JSON to create instances of `FixedTelephonyLists`.
 * - Extract `SupplementaryService` objects, which are nested within the JSON structure.
 *
 * Structure of the expected JSON:
 * - The adapter expects an object containing key-value pairs where "Servicios suplementarios"
 *   represents supplementary services, which are mapped to a list of `SupplementaryService` objects.
 */
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
