package io.github.suitetecsa.sdk.access.scraper

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.model.ConnectInformation
import io.github.suitetecsa.sdk.access.network.HttpResponse
import io.github.suitetecsa.sdk.exception.InvalidSessionException
import io.github.suitetecsa.sdk.exception.NautaException

/**
 * Interfaz que define un scraper para analizar contenido HTML en el portal de conexión.
 * Proporciona métodos para extraer información específica y transformarla en objetos y resultados.
 */
interface ConnectionInfoParser {
    /**
     * Analiza el HTML para verificar si hay conexiones disponibles.
     *
     * @param response El contenido HTML a analizar.
     * @return `true` si hay conexiones disponibles, de lo contrario, `false`.
     */
    fun parseCheckConnection(response: HttpResponse): Boolean

    /**
     * Analiza el HTML para extraer información de conexión de Nauta.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un objeto de tipo `ConnectInformation` que contiene la información de conexión.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    fun parseConnectInformation(httpResponse: HttpResponse): ConnectInformation

    /**
     * Analiza el HTML para extraer el tiempo restante de la conexión.
     *
     * @param httpResponse El contenido dela respuesta HTTP a analizar.
     * @return El tiempo restante de la conexión en segundos.
     */
    @Throws(InvalidSessionException::class)
    fun parseRemainingTime(httpResponse: HttpResponse): String

    /**
     * Analiza el HTML para extraer el atributo UUID.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El valor del atributo UUID.
     */
    @Throws(LoadInfoException::class)
    fun parseAttributeUUID(httpResponse: HttpResponse): String

    /**
     * Analiza el HTML para verificar si el cierre de sesión fue exitoso.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return `true` si el cierre de sesión fue exitoso, de lo contrario, `false`.
     */
    fun isSuccessLogout(httpResponse: HttpResponse): Boolean

    class Builder {
        fun build(): ConnectionInfoParser = ConnectionInfoParserImpl()
    }
}
