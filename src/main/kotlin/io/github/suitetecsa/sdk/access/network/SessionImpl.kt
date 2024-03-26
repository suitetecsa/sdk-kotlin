package io.github.suitetecsa.sdk.access.network

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.network.ConnectionFactory.createConnection
import io.github.suitetecsa.sdk.exception.NautaException
import org.jetbrains.annotations.Contract
import org.jsoup.Connection
import java.io.IOException

internal class SessionImpl : Session {
    private val cookies: MutableMap<String, String> = HashMap()

    /**
     * Crea y ejecuta una conexión con los parámetros proporcionados.
     *
     * @param url           URL de la solicitud.
     * @param requestData   Datos para la solicitud.
     * @param requestAction Función lambda que ejecuta la solicitud y devuelve la respuesta.
     * @return Objeto `ResultType<HttpResponse>` con los datos de la respuesta o información sobre el error, según
     * corresponda.
     </HttpResponse> */
    @Contract("_, _, _ -> new")
    @Throws(NautaException::class, LoadInfoException::class)
    private fun executeRequest(
        url: String,
        requestData: Map<String, String>?,
        requestAction: (Connection) -> Connection.Response
    ): HttpResponse {
        val connection = requestData?.let { createConnection(url, it, cookies) } ?: createConnection(url, cookies)
        return requestAction(connection).let {
            it.throwExceptionOnFailure("There was a failure to communicate with the portal")
            cookies.putAll(it.cookies())
            HttpResponse(it.bodyAsBytes())
        }
    }

    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url               URL a la que se realiza la solicitud.
     * @param params            Parámetros de la solicitud (opcional).
     * @param timeout           Tiempo límite para la solicitud (por defecto: `30000` milisegundos).
     * @return Objeto `ResultType<HttpResponse>` con los datos de la respuesta o información sobre el error, según
     * corresponda.
     */
    @Throws(NautaException::class, LoadInfoException::class, IOException::class)
    override fun get(url: String, params: Map<String, String>?, timeout: Int) =
        executeRequest(url, params) { it.timeout(timeout).method(Connection.Method.GET).execute() }

    /**
     * Sends a POST request to the given URL with the provided data.
     *
     * @param url The URL to send the POST request to.
     * @param data The data to include in the POST request body.
     * @return The HTTP response.
     * @throws NautaException If an error occurs in the Nauta SDK.
     * @throws LoadInfoException If an error occurs loading user info.
     */
    @Throws(NautaException::class, LoadInfoException::class, IOException::class)
    override fun post(url: String, data: Map<String, String>?) =
        executeRequest(url, data) { it.method(Connection.Method.POST).execute() }
}
