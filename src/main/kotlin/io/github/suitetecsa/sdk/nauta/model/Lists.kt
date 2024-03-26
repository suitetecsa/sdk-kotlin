package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

data class Lists(
    val plans: List<MobilePlan>,
    val bonuses: List<MobileBonus>,
)

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
