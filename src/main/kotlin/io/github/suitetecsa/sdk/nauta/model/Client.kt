package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

private const val EMAIL_INDEX = 0
private const val NAME_INDEX = 1
private const val MAIL_NOTIFICATIONS_INDEX = 2
private const val MOBILE_NOTIFICATIONS_INDEX = 3
private const val OPERATIONS_INDEX = 4
private const val PHONE_NUMBER_INDEX = 5
private const val PORTAL_USER_INDEX = 6

data class Client(
    val email: String,
    val name: String,
    val mailNotifications: String,
    val mobileNotifications: String,
    val operations: List<Operation>,
    val phoneNumber: String,
    val portalUser: String,
)

class ClientAdapter : JsonAdapter<Client>() {
    private val options: JsonReader.Options = JsonReader.Options.of(
        "email",
        "nombre",
        "notificaciones_mail",
        "notificaciones_movil",
        "operaciones",
        "telefono",
        "usuario_portal"
    )
    private val moshi: Moshi = Moshi.Builder()
        .add(Operation::class.java, OperationAdapter())
        .build()
    private val operationAdapter: JsonAdapter<Operation> = moshi.adapter(Operation::class.java)

    @Suppress("NestedBlockDepth")
    override fun fromJson(reader: JsonReader): Client {
        var email: String? = null
        var name: String? = null
        var mailNotifications: String? = null
        var mobileNotifications: String? = null
        val operations = mutableListOf<Operation>()
        var phoneNumber: String? = null
        var portalUser: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                EMAIL_INDEX -> email = reader.nextString()
                NAME_INDEX -> name = reader.nextString()
                MAIL_NOTIFICATIONS_INDEX -> mailNotifications = reader.nextString()
                MOBILE_NOTIFICATIONS_INDEX -> mobileNotifications = reader.nextString()
                OPERATIONS_INDEX -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        reader.skipName()
                        val operation = operationAdapter.fromJson(reader)
                        operation?.let { operations.add(it) }
                    }
                    reader.endObject()
                }
                PHONE_NUMBER_INDEX -> phoneNumber = reader.nextString()
                PORTAL_USER_INDEX -> portalUser = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Client(
            email!!,
            name!!,
            mailNotifications!!,
            mobileNotifications!!,
            operations,
            phoneNumber!!,
            portalUser!!
        )
    }

    override fun toJson(p0: JsonWriter, p1: Client?) {
        throw UnsupportedOperationException("toJson not supported")
    }
}
