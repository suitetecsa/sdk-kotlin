package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json

/**
 * Represents the different types of operations that can be performed in the system.
 *
 * This enum class maps various operation types to their JSON representations using
 * the `@Json` annotation from Moshi for serialization and deserialization purposes.
 *
 * Known operation types include:
 * - MODIFICATION
 * - CONSULT
 * - TOP_UP
 * - PASSWORD_RECOVERY
 * - PASSWORD_CHANGE
 * - TRANSFER
 * - SIGN_UP
 */
enum class OperationType {
    @Json(name = "MODIFICACION")
    MODIFICATION,

    @Json(name = "CONSULTA")
    CONSULT,

    @Json(name = "RECARGA")
    TOP_UP,

    @Json(name = "RECUPERAR_PASSWORD")
    PASSWORD_RECOVERY,

    @Json(name = "CAMBIO_PASSWORD")
    PASSWORD_CHANGE,

    @Json(name = "TRANSFERENCIA")
    TRANSFER,

    @Json(name = "ALTA")
    SIGN_UP
}
