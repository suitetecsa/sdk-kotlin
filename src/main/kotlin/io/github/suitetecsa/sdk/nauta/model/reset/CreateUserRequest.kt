package io.github.suitetecsa.sdk.nauta.model.reset

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * Represents a request to create a user with the associated information.
 *
 * @property phoneNumber The phone number of the user being created.
 * @property password The password for the user account.
 * @property dni The national identification number of the user.
 */
data class CreateUserRequest(val phoneNumber: String, val password: String, val dni: String)

/**
 * A custom JsonAdapter for serializing the CreateUserRequest into a JSON format
 * compatible with a specific API requirement.
 *
 * This adapter transforms a CreateUserRequest instance into a JSON object
 * containing a list of NautaActionRequestParam objects with specific keys and values.
 *
 * The adapter does not support deserialization (fromJson) and will throw
 * an UnsupportedOperationException if attempted.
 */
internal class CreateUserRequestAdapter : JsonAdapter<CreateUserRequest>() {
    override fun fromJson(p0: JsonReader): CreateUserRequest? {
        throw UnsupportedOperationException("fromJson not supported")
    }

    override fun toJson(writer: JsonWriter, request: CreateUserRequest?) {
        request?.let {
            val requestJson = CreateUserRequestJson(
                listOf(
                    NautaActionRequestParam("usuario", it.phoneNumber),
                    NautaActionRequestParam("password", it.password),
                    NautaActionRequestParam("noIdentidad", it.dni)
                )
            )
            writer.value(Gson().toJson(requestJson))
        } ?: run { writer.nullValue() }
    }
}

private data class CreateUserRequestJson(val param: List<NautaActionRequestParam>)
