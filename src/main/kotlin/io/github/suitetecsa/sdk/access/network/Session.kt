package io.github.suitetecsa.sdk.access.network

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.exception.NautaException

/**
 * Esta clase representa una sesión de comunicación con el portal Nauta (Portal Cautivo, Portal de Usuario).
 * Mantiene las cookies para permitir una comunicación continua.
 */
interface Session {
    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param params Parámetros de la solicitud (opcional).
     * @param timeout Tiempo límite para la solicitud (por defecto: `30000` milisegundos).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    fun get(url: String, params: Map<String, String>? = null, timeout: Int = 30000): HttpResponse

    /**
     * Realiza una solicitud POST al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param data Datos de la solicitud (opcional).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    fun post(url: String, data: Map<String, String>? = null): HttpResponse

    class Builder {
        fun build(): Session {
            return SessionImpl()
        }
    }
}