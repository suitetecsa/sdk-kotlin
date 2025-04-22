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

/**
 * Represents a client with contact and notification details, as well as a list of associated operations.
 *
 * @property email The email address of the client.
 * @property name The name of the client.
 * @property mailNotifications Notification settings for email-based messages.
 * @property mobileNotifications Notification settings for mobile-based messages.
 * @property operations A list of operations available for the client.
 * @property phoneNumber The phone number of the client.
 * @property portalUser The portal user identifier linked to the client.
 */
data class Client(
    val email: String,
    val name: String,
    val mailNotifications: String,
    val mobileNotifications: String,
    val operations: List<Operation>,
    val phoneNumber: String,
    val portalUser: String,
)

/**
 * A custom Moshi JsonAdapter implementation for serializing and deserializing instances of the `Client` class.
 *
 * This adapter handles JSON parsing for the `Client` data structure, defining how various fields
 * within the JSON object map to properties of the `Client` class.
 *
 * The adapter is responsible for reading a JSON object, mapping its fields to the appropriate
 * properties of the `Client` class, and constructing an instance of `Client`. It supports deserialization
 * (from JSON to `Client`) but does not implement serialization (from `Client` to JSON).
 *
 * @constructor Creates a new instance of `ClientAdapter`.
 *
 * This implementation is designed to work with a specific JSON schema where:
 * - The fields "email", "nombre", "notificaciones_mail", "notificaciones_movil", "operaciones",
 *   "telefono", and "usuario_portal" map to the respective properties of the `Client` class.
 * - The field "operaciones" represents a nested JSON object containing multiple operations.
 *   Each operation is deserialized using its own custom adapter, `OperationAdapter`.
 *
 * @see Client
 * @see Operation
 * @see OperationAdapter
 */
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
