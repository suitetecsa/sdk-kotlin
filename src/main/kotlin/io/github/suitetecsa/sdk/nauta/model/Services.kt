package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

private const val MAIL_SERVICES_INDEX = 0
private const val NAV_SERVICES_INDEX = 1
private const val MOBILE_SERVICES_INDEX = 2
private const val FIXED_TELEPHONY_SERVICES_INDEX = 3

/**
 * Represents a collection of different types of services.
 *
 * @property mailServices A list of mail services available.
 * @property navServices A list of navigation services available.
 * @property mobileServices A list of mobile services available.
 * @property fixedTelephony A list of fixed telephony services available.
 */
data class Services(
    val mailServices: List<MailService>,
    val navServices: List<NavService>,
    val mobileServices: List<MobileService>,
    val fixedTelephony: List<FixedTelephony>,
)

/**
 * A custom JSON adapter for deserializing objects of type `Services`.
 * This adapter is responsible for mapping the JSON structure to its corresponding
 * domain representation in the `Services` class.
 *
 * The class leverages Moshi's `JsonAdapter` for decoding nested service types such as
 * `MailService`, `NavService`, `MobileService`, and `FixedTelephony`. It supports a set
 * of predefined service options to identify and parse various service categories from
 * the provided JSON input.
 *
 * Features:
 * - Handles multiple nested service types and maps them to corresponding collections.
 * - Supports flexibility with options for mail, navigation, mobile, and fixed telephony services.
 * - Implements safe parsing using Moshi adapters for individual nested types.
 *
 * Constructor Details:
 * The class initializes with a `Moshi` instance paired with custom adapters to deserialize nested types,
 * along with an `options` instance to identify service categories in the JSON structure.
 *
 * Methods:
 * - fromJson: Reads the JSON input using `JsonReader` and maps the JSON tokens into the domain-specific `Services`
 * object.
 * - toJson: Throws an `UnsupportedOperationException` since serialization is not supported by this adapter.
 *
 * Parsing Details:
 * - Supports `BEGIN_OBJECT` type JSON structures where service categories are mapped to corresponding lists.
 * - In the case of a `STRING` JSON token, this method returns an empty `Services` object.
 * - Throws `JsonDataException` for unexpected JSON tokens during parsing.
 */
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

        when (reader.peek()) {
            JsonReader.Token.STRING -> {
                reader.nextString()
                return Services(emptyList(), emptyList(), emptyList(), emptyList())
            }

            JsonReader.Token.BEGIN_OBJECT -> {
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
            }

            else -> throw JsonDataException("Unexpected token: ${reader.peek()}")
        }

        return Services(mailServices, navServices, mobileServices, fixedTelephony)
    }

    override fun toJson(p0: JsonWriter, p1: Services?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
