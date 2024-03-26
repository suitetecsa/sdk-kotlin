package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json

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
