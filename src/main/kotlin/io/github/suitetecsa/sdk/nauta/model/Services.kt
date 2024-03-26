package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

private const val MAIL_SERVICES_INDEX = 0
private const val NAV_SERVICES_INDEX = 1
private const val MOBILE_SERVICES_INDEX = 2
private const val FIXED_TELEPHONY_SERVICES_INDEX = 3

data class Services(
    val mailServices: List<MailService>,
    val navServices: List<NavService>,
    val mobileServices: List<MobileService>,
    val fixedTelephony: List<FixedTelephony>,
)

class ServicesAdapter : JsonAdapter<Services>() {
    private val options: JsonReader.Options = JsonReader.Options.of(
        "Correo Nauta",
        "Navegación",
        "Servicios móviles",
        "Telefonía fija"
    )
    private val moshi: Moshi = Moshi.Builder()
        .add(MailService::class.java, MailServiceAdapter())
        .add(NavService::class.java, NavServiceAdapter())
        .add(Lists::class.java, ListsAdapter())
        .add(FixedTelephony::class.java, FixedTelephonyAdapter())
        .build()
    private val mailServiceAdapter: JsonAdapter<MailService> = moshi.adapter(MailService::class.java)
    private val navServiceAdapter: JsonAdapter<NavService> = moshi.adapter(NavService::class.java)
    private val mobileServiceAdapter: JsonAdapter<MobileService> = moshi.adapter(MobileService::class.java)
    private val fixedTelephonyAdapter: JsonAdapter<FixedTelephony> = moshi.adapter(FixedTelephony::class.java)

    @Suppress("NestedBlockDepth", "CyclomaticComplexMethod")
    override fun fromJson(reader: JsonReader): Services {
        val mailServices = mutableListOf<MailService>()
        val navServices = mutableListOf<NavService>()
        val mobileServices = mutableListOf<MobileService>()
        val fixedTelephony = mutableListOf<FixedTelephony>()

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                MAIL_SERVICES_INDEX -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val mailService = mailServiceAdapter.fromJson(reader)
                        mailService?.let { mailServices.add(it) }
                    }
                    reader.endObject()
                }
                NAV_SERVICES_INDEX -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val navService = navServiceAdapter.fromJson(reader)
                        navService?.let { navServices.add(it) }
                    }
                    reader.endObject()
                }
                MOBILE_SERVICES_INDEX -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val mobileService = mobileServiceAdapter.fromJson(reader)
                        mobileService?.let { mobileServices.add(it) }
                    }
                    reader.endObject()
                }
                FIXED_TELEPHONY_SERVICES_INDEX -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val telephony = fixedTelephonyAdapter.fromJson(reader)
                        telephony?.let { fixedTelephony.add(it) }
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Services(mailServices, navServices, mobileServices, fixedTelephony)
    }

    override fun toJson(p0: JsonWriter, p1: Services?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
