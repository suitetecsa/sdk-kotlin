package io.github.suitetecsa.sdk.network

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.network.AccessRoute
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.exception.NautaException
import java.util.function.Function

private const val DEFAULT_TIMEOUT = 30000

/**
 * Clase que implementa el comunicador del portal utilizando Jsoup.
 */
internal class PortalCommunicatorImpl(private val session: Session) : PortalCommunicator {
    /**
     * Maneja la respuesta de una solicitud al portal y la transforma según una función dada.
     *
     * @param url               La URL a la que se realiza la solicitud.
     * @param data              Datos para la solicitud (opcional).
     * @param method            El método HTTP utilizado para la solicitud (por defecto es GET).
     * @param timeout           El tiempo límite para la solicitud en milisegundos (por defecto es 30000).
     * @param transform         La función de transformación que se aplicará a la respuesta del portal.
     * @return Resultado encapsulado en un objeto `ResultType` que contiene el resultado transformado o un error.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    fun <T> handleResponse(
        url: String,
        data: Map<String, String>?,
        method: HttpMethod,
        timeout: Int,
        transform: Function<HttpResponse, T>
    ): T {
        val response = when (method) {
            HttpMethod.POST -> session.post(url, data ?: mapOf())
            HttpMethod.GET -> session.get(url, data ?: mapOf(), timeout)
        }
        return transform.apply(response)
    }

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta utilizando la sesión dada.
     *
     * @param route    La acción que se va a realizar en el portal.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    override fun <T> performRequest(route: AccessRoute, transform: Function<HttpResponse, T>): T {
        return handleResponse(
            route.url,
            route.data,
            route.method,
            route.timeout,
            transform
        )
    }

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta utilizando la URL dada.
     *
     * @param url       La URL a la que se realiza la solicitud.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    override fun <T> performRequest(url: String, transform: Function<HttpResponse, T>): T {
        return handleResponse(url, null, HttpMethod.GET, DEFAULT_TIMEOUT, transform)
    }
}
