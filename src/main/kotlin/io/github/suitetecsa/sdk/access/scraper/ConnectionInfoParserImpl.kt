package io.github.suitetecsa.sdk.access.scraper

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.model.AccountInfo
import io.github.suitetecsa.sdk.access.model.ConnectInformation
import io.github.suitetecsa.sdk.access.model.LastConnection
import io.github.suitetecsa.sdk.network.HttpResponse
import io.github.suitetecsa.sdk.access.utils.ExceptionHandler
import io.github.suitetecsa.sdk.exception.NautaException
import io.github.suitetecsa.sdk.exception.InvalidSessionException
import org.jetbrains.annotations.Contract
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.math.min

private const val NOT_LAST_CONNECTION_KEYS = 4
private const val CONNECT_DOMAIN = "secure.etecsa.net"

/**
 * Implementación de `ConnectionInfoParser` para analizar información de conexión de Nauta.
 */
internal class ConnectionInfoParserImpl : ConnectionInfoParser {
    private val loadInfoExceptionHandler = ExceptionHandler { LoadInfoException(it) }

    /**
     * Analiza la tabla de información en el HTML y extrae los valores asociados a las claves dadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Un mapa de pares clave-valor con la información extraída de la tabla.
     */
    private fun parseInfoTable(htmlParsed: Document, keys: List<String>): Map<String, String> {
        val info: MutableMap<String, String> = HashMap()
        val valueElements = htmlParsed.select("#sessioninfo > tbody > tr > :not(td.key)")

        val size = min(keys.size.toDouble(), valueElements.size.toDouble()).toInt()

        for (index in 0 until size) {
            val element = valueElements[index]
            info[keys[index]] = element.text().trim { it <= ' ' }
        }

        return info
    }

    /**
     * Analiza la tabla de últimas conexiones en el HTML y extrae los valores de las columnas especificadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Una lista de objetos `LastConnection` con la información de las últimas conexiones.
     */
    private fun parseLastConnectionsTable(htmlParsed: Document, keys: List<String>): List<LastConnection> {
        val lastConnections: MutableList<LastConnection> = ArrayList()
        val connectionRows = htmlParsed.select("#sesiontraza > tbody > tr")

        for (row in connectionRows) {
            val connectionValues = row.select("td")
            val connectionMap: MutableMap<String, String> = HashMap()

            for (index in NOT_LAST_CONNECTION_KEYS until keys.size) {
                val element = connectionValues[index - NOT_LAST_CONNECTION_KEYS]
                connectionMap[keys[index]] = element.text().trim { it <= ' ' }
            }

            lastConnections.add(
                LastConnection(
                    connectionMap.getOrDefault("from", ""),
                    connectionMap.getOrDefault("accountTime", ""),
                    connectionMap.getOrDefault("to", "")
                )
            )
        }

        return lastConnections
    }

    /**
     * Crea un objeto `AccountInfo` a partir del mapa de información proporcionado.
     *
     * @param info El mapa de información con claves y valores asociados.
     * @return Un objeto `AccountInfo` con la información de la cuenta.
     */
    @Contract("_ -> new")
    private fun createAccountInfo(info: Map<String, String>): AccountInfo {
        return AccountInfo(
            info.getOrDefault("access_areas", ""),
            info.getOrDefault("account_status", ""),
            info.getOrDefault("availableBalance", ""),
            info.getOrDefault("expiration_date", "")
        )
    }

    /**
     * Analiza el HTML para verificar si hay conexiones disponibles.
     *
     * @param response El contenido HTML a analizar.
     * @return `true` si hay conexiones disponibles, de lo contrario, `false`.
     */
    override fun parseCheckConnection(response: HttpResponse) = !response.text.contains(CONNECT_DOMAIN)

    /**
     * Analiza el HTML para extraer información de conexión de Nauta.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un objeto de tipo `NautaConnectInformation` que contiene la información de conexión.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    override fun parseConnectInformation(httpResponse: HttpResponse): ConnectInformation {
        val keys: List<String> = mutableListOf(
            "account_status",
            "availableBalance",
            "expiration_date",
            "access_areas",
            "from",
            "to",
            "accountTime"
        )
        val htmlParsed = Jsoup.parse(httpResponse.text)

        ErrorParser.Builder().build().throwExceptionOnFailure(
            httpResponse,
            "Fail parse nauta account information",
            loadInfoExceptionHandler
        )

        val info = parseInfoTable(htmlParsed, keys)
        val lastConnections = parseLastConnectionsTable(htmlParsed, keys)

        return ConnectInformation(createAccountInfo(info), lastConnections)
    }

    /**
     * Analiza el HTML para extraer el tiempo restante de la conexión.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El tiempo restante de la conexión en segundos.
     */
    @Throws(InvalidSessionException::class)
    override fun parseRemainingTime(httpResponse: HttpResponse) =
        httpResponse.text.takeIf {
            it.lowercase().contains("errorop")
        } ?: throw InvalidSessionException("The provided session data is no longer valid.")

    /**
     * Analiza el HTML para extraer el atributo UUID.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El valor del atributo UUID.
     */
    @Throws(LoadInfoException::class)
    override fun parseAttributeUUID(httpResponse: HttpResponse) =
        """ATTRIBUTE_UUID=(\w+)&""".toRegex().find(httpResponse.text)?.let {
            it.groups[1]?.value
        } ?: throw loadInfoExceptionHandler.handleException("Fail to parse attribute uuid", listOf(httpResponse.text))

    override fun isSuccessLogout(httpResponse: HttpResponse) = httpResponse.text.contains("SUCCESS")
}
