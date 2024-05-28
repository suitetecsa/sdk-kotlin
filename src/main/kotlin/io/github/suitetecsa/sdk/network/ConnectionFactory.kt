package io.github.suitetecsa.sdk.network

import org.jsoup.Connection
import org.jsoup.Jsoup

/**
 * Implementación de la factoría de conexiones utilizando Jsoup.
 */
object ConnectionFactory {
    private const val USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:97.0) Gecko/20100101 Firefox/97.0"
    private val headers: Map<String, String> = java.util.Map.of(
        "Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Encoding",
        "gzip, deflate, br",
        "Accept-Language",
        "es-MX,es;q=0.8,en-US;q=0.5,en;q=0.3"
    )

    /**
     * Crea y devuelve una conexión utilizando la URL y los datos proporcionados.
     *
     * @param url         La URL a la que se va a establecer la conexión.
     * @param cookies     Cookies para la solicitud (opcional).
     * @return Objeto `Connection` que representa la conexión creada.
     */
    fun createConnection(url: String, cookies: Map<String, String>?): Connection {
        return createConnection(url, null, cookies)
    }

    /**
     * Crea y devuelve una conexión utilizando la URL y los datos proporcionados.
     *
     * @param url         La URL a la que se va a establecer la conexión.
     * @param requestData Datos para la solicitud (opcional).
     * @param cookies     Cookies para la solicitud (opcional).
     * @return Objeto `Connection` que representa la conexión creada.
     */
    fun createConnection(
        url: String,
        requestData: Map<String, String>?,
        cookies: Map<String, String>?
    ): Connection {
        val connection = Jsoup.connect(url)
        connection.userAgent(USER_AGENT)
        connection.headers(headers)

        if (requestData != null) {
            connection.data(requestData)
        }

        if (!cookies.isNullOrEmpty()) {
            connection.cookies(cookies)
        }

        return connection
    }
}
