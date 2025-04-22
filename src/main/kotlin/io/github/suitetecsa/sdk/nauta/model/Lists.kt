package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

/**
 * Represents a collection of lists containing mobile plans and bonuses associated with a user.
 *
 * @property plans A list of mobile plans detailing data allowances, type, and expiration dates.
 * @property bonuses A list of mobile bonuses detailing data allowances, type, start dates, and expiration dates.
 */
data class Lists(
    val plans: List<MobilePlan>,
    val bonuses: List<MobileBonus>,
)

/**
 * A custom adapter class for deserializing JSON data into the `Lists` object structure using Moshi.
 *
 * The `ListsAdapter` handles the parsing of JSON data containing two main properties:
 * - "Planes": Represents a collection of `MobilePlan` objects.
 * - "Bonos": Represents a collection of `MobileBonus` objects.
 *
 * This adapter focuses on deserialization as `fromJson` is implemented, while serialization is explicitly
 * not supported and throws an `UnsupportedOperationException` if attempted through `toJson`.
 *
 * The main functionality provided by this adapter includes:
 * - Parsing nested JSON fields into two lists: a list of `MobilePlan` objects and a list of `MobileBonus` objects.
 * - Skipping unrecognized or unused JSON fields during the deserialization process.
 * - Handling the nested structure of JSON objects for the "Planes" and "Bonos" keys.
 *
 * The parsed lists are encapsulated into a `Lists` object, which is returned by the `fromJson` function.
 *
 * Internally, this class uses Moshi to handle the deserialization of individual `MobilePlan` and `MobileBonus` objects.
 *
 * @constructor Creates an instance of `ListsAdapter`.
 *
 * See also:
 * - `Lists`
 * - `MobilePlan`
 * - `MobileBonus`
 *
 * @throws UnsupportedOperationException when attempting to serialize data with `toJson`.
 */
class ListsAdapter : JsonAdapter<Lists>() {
    private val options: JsonReader.Options = JsonReader.Options.of("Planes", "Bonos")
    private val moshi: Moshi = Moshi.Builder().build()
    private val mobilePlanAdapter: JsonAdapter<MobilePlan> = moshi.adapter(MobilePlan::class.java)
    private val mobileBonusAdapter: JsonAdapter<MobileBonus> = moshi.adapter(MobileBonus::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): Lists {
        val plans = mutableListOf<MobilePlan>()
        val bonuses = mutableListOf<MobileBonus>()

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val plan = mobilePlanAdapter.fromJson(reader)
                        plan?.let { plans.add(it) }
                    }
                    reader.endObject()
                }

                1 -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val bonus = mobileBonusAdapter.fromJson(reader)
                        bonus?.let { bonuses.add(it) }
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Lists(plans, bonuses)
    }

    override fun toJson(p0: JsonWriter, p1: Lists?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
